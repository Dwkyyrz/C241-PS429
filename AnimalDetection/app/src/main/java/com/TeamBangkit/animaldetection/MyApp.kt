package com.TeamBangkit.animaldetection

import android.app.Application
import androidx.room.Room
import com.TeamBangkit.animaldetection.Data.DB.AppDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "animal_classification_db"
        ).build()
    }
}