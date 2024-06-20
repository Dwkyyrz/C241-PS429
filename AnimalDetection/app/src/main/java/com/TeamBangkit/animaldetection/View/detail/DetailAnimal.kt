package com.TeamBangkit.animaldetection.View.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.TeamBangkit.animaldetection.MainActivity
import com.TeamBangkit.animaldetection.R

class DetailAnimal : AppCompatActivity() {

    private var animalName: String? = null
    private var animalYears: String? = null
    private var animalTipe: String? = null
    private var animalZoo: String? = null
    private var animalDescription: String? = null
    private var animalPhoto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Detail Hewan"
        }


//        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_animal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val animalName = intent.getStringExtra("animal_name")
        val animalYears = intent.getStringExtra("animal_years")
        val animalTipe = intent.getStringExtra("animal_tipe")
        val animalZoo = intent.getStringExtra("animal_zoo")
        val animalDescription = intent.getStringExtra("animal_description")
        val animalPhoto = intent.getIntExtra("animal_photo", 0)

        val tvAnimalName = findViewById<TextView>(R.id.nama_hewan)
        val tvAnimalYears = findViewById<TextView>(R.id.animal_years)
        val tvAnimalTipe = findViewById<TextView>(R.id.tipe_animal)
        val tvAnimalZoo = findViewById<TextView>(R.id.animal_zoo)
        val tvAnimalDescription = findViewById<TextView>(R.id.detail_deskripsi)
        val imgMoviePhoto = findViewById<ImageView>(R.id.detail_poster)

        tvAnimalName.text = animalName
        tvAnimalYears.text = animalYears
        tvAnimalTipe.text = animalTipe
        tvAnimalZoo.text = animalZoo
        tvAnimalDescription.text = animalDescription
        imgMoviePhoto.setImageResource(animalPhoto)
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