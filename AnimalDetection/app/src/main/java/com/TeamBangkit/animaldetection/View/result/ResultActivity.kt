package com.TeamBangkit.animaldetection.View.result

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.TeamBangkit.animaldetection.Data.DB.ClassificationHistory
import com.TeamBangkit.animaldetection.Data.retrofit.API.ApiConfig
import com.TeamBangkit.animaldetection.Data.retrofit.Response.PredictionResponse
import com.TeamBangkit.animaldetection.FileUtil
import com.TeamBangkit.animaldetection.MyApplication
import com.TeamBangkit.animaldetection.databinding.ActivityResultBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream


class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Hasil Klasifikasi"
        }
        enableEdgeToEdge()

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageUri?.let {
            binding.previewResultImageView.setImageURI(it)
            uploadImage(it)
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val progressBarResult: ProgressBar = binding.progressbarResult
        progressBarResult.visibility = View.VISIBLE
        binding.tvResult.text = ""
        binding.tvDeskripsi.text = ""
        binding.tvUmur.text = ""
        binding.tvProbability.text = ""
        val file = FileUtil.from(this, imageUri)
        if (file != null) {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val apiService = ApiConfig.getApiService()
            apiService.predictAnimal(body).enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                    if (response.isSuccessful) {
                        val prediction = response.body()
                        prediction?.let {
                            progressBarResult.visibility = View.INVISIBLE
                            binding.tvResult.text = "${it.data.result}"
                            binding.tvDeskripsi.text = "Deskripsi : \n\n${it.data.description}"
                            binding.tvUmur.text = "Rata-rata hidup selama : ${it.data.age} tahun"
                            binding.tvProbability.text = "Probability : ${it.data.probability.toInt()}%"

                            val bitmap = BitmapFactory.decodeFile(file.path)
                            val imagePath = saveImageToInternalStorage(bitmap, this@ResultActivity)

                            saveClassificationResult(
                                imagePath,
//                                imageUri.toString(),
                                it.data.result,
                                it.data.description,
                                it.data.age,
                                it.data.probability
                            )
                        }
                    } else {
                        progressBarResult.visibility = View.INVISIBLE
                        binding.tvResult.text = "Prediction failed: ${response.errorBody()?.string()}"
                        binding.tvDeskripsi.text = ""
                        binding.tvUmur.text = ""
                        binding.tvProbability.text = ""
                        Log.e("ResultActivity", "Prediction failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    binding.tvResult.text = "Error: ${t.message}"
                    binding.tvDeskripsi.text = ""
                    binding.tvUmur.text = ""
                    binding.tvProbability.text = ""
                    Log.e("ResultActivity", "Error: ${t.message}", t)
                    progressBarResult.visibility = View.INVISIBLE
                }
            })
        } else {
            progressBarResult.visibility = View.INVISIBLE
            binding.tvResult.text = "Error: Unable to process the image"
            binding.tvDeskripsi.text = ""
            binding.tvUmur.text = ""
            binding.tvProbability.text = ""
            Log.e("ResultActivity", "Error: Unable to process the image")
        }
    }

    private fun saveClassificationResult(imageUri: String, name: String, description: String, age: Int, probability: Float) {
        val history = ClassificationHistory(
            imageUri = imageUri,
            name = name,
            description = description,
            age = age.toString(),
            probability = probability
        )

        lifecycleScope.launch {
            MyApplication.database.classificationHistoryDao().insert(history)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Closes the current activity and returns to the previous one
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

fun saveImageToInternalStorage(bitmap: Bitmap, context: Context): String {
    val filename = "${System.currentTimeMillis()}.jpg"
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        fileOutputStream?.close()
    }
    return "${context.filesDir}/$filename"
}
