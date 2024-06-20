package com.TeamBangkit.animaldetection.View.Classify

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TeamBangkit.animaldetection.View.result.ResultActivity
import com.TeamBangkit.animaldetection.databinding.FragmentClassifyBinding
import com.TeamBangkit.animaldetection.getImageUri
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ClassifyFragment : Fragment() {

    private var _binding: FragmentClassifyBinding? = null
    private var currentImageUri: Uri? = null
    private var currentPhotoPath: String? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ClassifyViewModel::class.java)

        _binding = FragmentClassifyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.classificationGaleri.setOnClickListener{openFileImg()}
        binding.classificationCamera.setOnClickListener { startCamera() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let { uri ->
                analyzeImage(uri)
            } ?: run {
                Log.d("ClassifyFragment", "No image selected for analysis")
            }
        }

        dashboardViewModel.text.observe(viewLifecycleOwner) { }
        return root
    }

    private fun openFileImg() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImgFile()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImgFile() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // Izin kamera telah diberikan, lanjutkan untuk memulai kamera
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.e("ClassifyFragment", "Error creating image file", ex)
                null
            }

            photoFile?.also {
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.TeamBangkit.animaldetection.fileprovider",
                    it
                )
                currentImageUri = photoUri
                launcherIntentCamera.launch(photoUri)
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImgFile()
        }
    }

    private fun compressImage(imageUri: Uri): Bitmap? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            val maxSizeBytes = 1024 * 1024 // 1MB max file size
            var compressQuality = 100 // Initial compression quality
            var outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
            while (outputStream.toByteArray().size > maxSizeBytes && compressQuality > 10) {
                outputStream = ByteArrayOutputStream()
                compressQuality -= 10
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
            }
            ByteArrayInputStream(outputStream.toByteArray()).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            Log.e("ClassifyFragment", "Error compressing image", e)
            null
        }
    }

    private fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }



    private fun analyzeImage(imageUri: Uri) {
        val compressedBitmap = compressImage(imageUri)
        if (compressedBitmap != null) {
            // Convert compressedBitmap to Uri and pass to ResultActivity
            val compressedUri = bitmapToUri(requireContext(), compressedBitmap)
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra("imageUri", compressedUri.toString())
            startActivity(intent)
        } else {
            Log.d("ClassifyFragment", "Failed to compress image")
            // Handle failure to compress image
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Log.d("Camera", "Permission denied")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}