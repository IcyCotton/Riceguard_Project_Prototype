package com.example.riceguard_project_prototype.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnosis_results")
data class DiagnosisResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diseaseName: String,
    val confidenceScore: Float,
    val timestamp: Long,
    val latitude: Double?,
    val longitude: Double?,
    val imageUrl: String?
)
