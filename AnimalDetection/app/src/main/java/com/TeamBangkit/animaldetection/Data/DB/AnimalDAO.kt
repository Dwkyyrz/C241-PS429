package com.TeamBangkit.animaldetection.Data.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClassificationHistoryDao {
    @Insert
    suspend fun insert(history: ClassificationHistory)

    @Query("SELECT * FROM classification_history")
    suspend fun getAll(): List<ClassificationHistory>

    @Delete
    suspend fun delete(history: ClassificationHistory)
}
