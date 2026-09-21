package com.example.riceguard_project_prototype.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DiagnosisDaoTest {

    private lateinit var db: RiceGuardDatabase
    private lateinit var dao: DiagnosisDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, RiceGuardDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.diagnosisDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetAllResults() = runBlocking {
        val result = DiagnosisResult(
            id = 1,
            diseaseName = "Blast",
            confidenceScore = 0.92f,
            timestamp = System.currentTimeMillis(),
            latitude = 13.4125,
            longitude = 122.5641,
            imageUrl = "file://scan_1.png"
        )
        
        dao.insertResult(result)
        val allResults = dao.getAllResults().first()
        
        assertEquals(1, allResults.size)
        assertEquals("Blast", allResults[0].diseaseName)
        assertEquals(0.92f, allResults[0].confidenceScore)
        assertEquals(13.4125, allResults[0].latitude ?: 0.0, 0.0001)
        assertEquals(122.5641, allResults[0].longitude ?: 0.0, 0.0001)
        assertEquals("file://scan_1.png", allResults[0].imageUrl)
    }

    @Test
    fun deleteResult() = runBlocking {
        val result = DiagnosisResult(
            id = 2,
            diseaseName = "Healthy",
            confidenceScore = 0.99f,
            timestamp = System.currentTimeMillis(),
            latitude = null,
            longitude = null,
            imageUrl = null
        )
        
        dao.insertResult(result)
        var allResults = dao.getAllResults().first()
        assertEquals(1, allResults.size)
        
        dao.deleteResult(allResults[0])
        allResults = dao.getAllResults().first()
        assertEquals(0, allResults.size)
    }
}
