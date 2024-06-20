package com.TeamBangkit.animaldetection.Data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification_history")
data class ClassificationHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String,
    val name: String,
    val description: String,
    val age: String,
    val probability: Float
)