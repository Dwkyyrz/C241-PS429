package com.TeamBangkit.animaldetection.View.History.detail_item

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.TeamBangkit.animaldetection.R
import com.TeamBangkit.animaldetection.databinding.ActivityHistoryDetailBinding


class HistoryDetail : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Detail Hewan"
        }
//        enableEdgeToEdge()

        val imageUri = intent.getStringExtra("imageUri")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val age = intent.getStringExtra("age")
        val probability = intent.getStringExtra("probability")

//        Glide.with(this).load(imageUri).into(binding.imgDetailPhoto)
        Glide.with(this)
            .load(imageUri)
            .error(R.drawable.ic_error_image)
            .into(binding.imgDetailPhoto)
        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = description
        binding.tvDetailAge.text = age
        binding.tvDetailProbability.text = probability
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