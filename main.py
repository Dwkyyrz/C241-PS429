from flask import Flask, request, jsonify
import uuid
from datetime import datetime
from werkzeug.utils import secure_filename
import tensorflow as tf
import numpy as np
from google.cloud import firestore
import os
import requests

app = Flask(__name__)

# Set environment variable for Google Cloud credentials
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "./c241-ps429-capstone-6b36f18f7d0a.json"

# Initialize Firestore client
db = firestore.Client()

MAX_CONTENT_LENGTH = 1 * 1024 * 1024  # 1MB

# URL publik ke file JSON di Google Cloud Storage
PUBLIC_URL = "https://storage.googleapis.com/bucket-kategori-hewan/kategori.json"

# Fungsi untuk memuat model dari URL publik
def load_model_from_local():
    try:
        model_path = "animals_model.h5"
        model = tf.keras.models.load_model(model_path, compile=False)
        print("Model loaded successfully")
        return model
    except Exception as e:
        print(f"Error during model loading: {e}")
        return None

# Muat model sekali saat aplikasi mulai
model = load_model_from_local()

# Fungsi untuk mengambil data kategori dari URL publik
def get_category_data():
    try:
        response = requests.get(PUBLIC_URL)
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(f"Error fetching category data: {e}")
        return None

# Ambil data kategori saat aplikasi mulai
category_data = get_category_data()

@app.route("/predict", methods=["POST"])
def predict():
    if "image" not in request.files:
        return jsonify({"status": "fail", "message": "No file part"}), 400

    file = request.files["image"]

    if file.filename == "":
        return jsonify({"status": "fail", "message": "No selected file"}), 400

    if file and allowed_file(file.filename):
        if request.content_length > MAX_CONTENT_LENGTH:
            return jsonify(
                {
                    "status": "fail",
                    "message": "Payload content length greater than maximum allowed: 1000000",
                }
            ), 413

        secure_filename(file.filename)
        try:
            if model is None:
                raise Exception("Model not loaded")
            print("Predicting the image")
            result, probability = model_predict(file)
            print(f"Prediction result: {result}")
            response_id = str(uuid.uuid4())
            
            if category_data is None:
                return jsonify({"status": "fail", "message": "Category data not found"}), 400
            
            category_info = category_data.get(result, {})
            description = category_info.get("description", "Description not found")
            age = category_info.get("age", "Age not found")
            
            response_data = {
                "id": response_id,
                "result": result,
                "description": description,
                "probability": round(probability, 2),  # Membulatkan probabilitas hingga dua angka desimal
                "age": age,
                "createdAt": datetime.utcnow().isoformat() + "Z",
            }

            # Save prediction result to Firestore
            save_to_firestore(response_id, response_data)

            return jsonify(
                {
                    "status": "success",
                    "message": "Model is predicted successfully",
                    "data": response_data,
                }
            ), 201
        except Exception as e:
            print(f"Prediction error: {e}")
            return jsonify(
                {
                    "status": "fail",
                    "message": "Terjadi kesalahan dalam melakukan prediksi",
                }
            ), 400
    else:
        return jsonify({"status": "fail", "message": "Invalid file format"}), 400

def allowed_file(filename):
    ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg"}
    return "." in filename and filename.rsplit(".", 1)[1].lower() in ALLOWED_EXTENSIONS

def model_predict(file):
    try:
        # Proses file menjadi array yang bisa diterima model
        img = tf.io.decode_jpeg(file.read(), channels=3)
        img = tf.image.resize(img, [224, 224])  # Sesuaikan ukuran sesuai model Anda
        img = tf.expand_dims(img, 0)  # Tambahkan dimensi batch
        img = tf.cast(img, tf.float32) / 255.0  # Normalisasi gambar
        print("Image processed for prediction")
        
        # Dapatkan prediksi model
        predictions = model.predict(img)
        print(f"Raw model predictions: {predictions}")
        
        # Dapatkan skor probabilitas dan kelas dengan probabilitas tertinggi menggunakan TensorFlow
        probabilities = tf.nn.softmax(predictions)[0]
        confidence_score = tf.reduce_max(probabilities).numpy() * 100
        predicted_class = tf.argmax(probabilities).numpy()
        categories = list(category_data.keys())  # Mengambil kategori dari data yang diambil dari GCS
        result = categories[predicted_class]
        print(f"Predicted class: {result}, confidence score: {confidence_score} %")
        
        return result, confidence_score
    except Exception as e:
        print(f"Error in model_predict: {e}")
        raise

def save_to_firestore(doc_id, data):
    try:
        # Tentukan nama koleksi Firestore
        collection_name = "predictions"
        
        # Tambahkan data ke koleksi dengan doc_id sebagai nama dokumen
        db.collection(collection_name).document(doc_id).set(data)
        print("Data saved to Firestore successfully")
    except Exception as e:
        print(f"Error saving to Firestore: {e}")
        raise

@app.route("/predict/<doc_id>", methods=["GET"])
def get_prediction(doc_id):
    try:
        # Ambil dokumen dari koleksi 'predictions' berdasarkan doc_id
        doc_ref = db.collection("predictions").document(doc_id)
        doc = doc_ref.get()
        if doc.exists:
            return jsonify({"status": "success", "data": doc.to_dict()}), 200
        else:
            return jsonify({"status": "fail", "message": "Document not found"}), 404
    except Exception as e:
        print(f"Error fetching document: {e}")
        return jsonify({"status": "fail", "message": "Error fetching document"}), 500

@app.route("/predict/histories", methods=["GET"])
def get_all_predictions():
    try:
        # Ambil semua dokumen dari koleksi 'predictions'
        predictions_ref = db.collection("predictions")
        docs = predictions_ref.stream()
        predictions = []
        for doc in docs:
            predictions.append(doc.to_dict())
        
        return jsonify({"status": "success", "data": predictions}), 200
    except Exception as e:
        print(f"Error fetching documents: {e}")
        return jsonify({"status": "fail", "message": "Error fetching documents"}), 500

if __name__ == "__main__":
    app.run(debug=True)
