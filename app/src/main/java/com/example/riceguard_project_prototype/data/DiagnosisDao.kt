package com.example.riceguard_project_prototype.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: DiagnosisResult): Long

    @Query("SELECT * FROM diagnosis_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<DiagnosisResult>>

    @Delete
    suspend fun deleteResult(result: DiagnosisResult): Int
}
