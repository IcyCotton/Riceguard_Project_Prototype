package com.example.riceguard_project_prototype.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class DiagnosisRepositoryTest {

    private class FakeDiagnosisDao : DiagnosisDao {
        private val resultsMap = mutableMapOf<Int, DiagnosisResult>()
        private val _resultsFlow = MutableStateFlow<List<DiagnosisResult>>(emptyList())
        private var idCounter = 1

        private fun updateFlow() {
            _resultsFlow.value = resultsMap.values.sortedByDescending { it.timestamp }
        }

        override suspend fun insertResult(result: DiagnosisResult): Long {
            val finalId = if (result.id == 0) idCounter++ else result.id
            val inserted = result.copy(id = finalId)
            resultsMap[finalId] = inserted
            updateFlow()
            return finalId.toLong()
        }

        override fun getAllResults(): Flow<List<DiagnosisResult>> {
            return _resultsFlow
        }

        override suspend fun deleteResult(result: DiagnosisResult): Int {
            val removed = resultsMap.remove(result.id)
            updateFlow()
            return if (removed != null) 1 else 0
        }
    }

    @Test
    fun testRepositoryOperations() = runBlocking {
        val fakeDao = FakeDiagnosisDao()
        val repository = DiagnosisRepository(fakeDao)

        // 1. Initial state should be empty
        var currentList = repository.getAllResults().first()
        assertEquals(0, currentList.size)

        // 2. Insert a result
        val result = DiagnosisResult(
            diseaseName = "Brown Spot",
            confidenceScore = 0.88f,
            timestamp = 1625097600000L,
            latitude = 14.5995,
            longitude = 120.9842,
            imageUrl = "path/to/image.jpg"
        )
        val id = repository.insertResult(result)
        assertEquals(1L, id)

        // 3. Verify it was inserted and fetched correctly
        currentList = repository.getAllResults().first()
        assertEquals(1, currentList.size)
        assertEquals("Brown Spot", currentList[0].diseaseName)
        assertEquals(0.88f, currentList[0].confidenceScore)
        assertEquals(14.5995, currentList[0].latitude ?: 0.0, 0.001)

        // 4. Delete the result
        val deleteCount = repository.deleteResult(currentList[0])
        assertEquals(1, deleteCount)

        // 5. Verify it's empty again
        currentList = repository.getAllResults().first()
        assertEquals(0, currentList.size)
    }
}
