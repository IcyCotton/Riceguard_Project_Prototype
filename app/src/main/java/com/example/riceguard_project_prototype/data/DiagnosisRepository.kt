package com.example.riceguard_project_prototype.data

import kotlinx.coroutines.flow.Flow

class DiagnosisRepository(private val diagnosisDao: DiagnosisDao) {
    fun getAllResults(): Flow<List<DiagnosisResult>> = diagnosisDao.getAllResults()

    suspend fun insertResult(result: DiagnosisResult): Long = diagnosisDao.insertResult(result)

    suspend fun deleteResult(result: DiagnosisResult): Int = diagnosisDao.deleteResult(result)
}
