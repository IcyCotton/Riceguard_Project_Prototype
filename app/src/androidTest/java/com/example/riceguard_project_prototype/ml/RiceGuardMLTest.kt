package com.example.riceguard_project_prototype.ml

import android.graphics.Bitmap
import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.riceguard_project_prototype.data.dataset.DatasetManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RiceGuardMLTest {

    @Test
    fun testCNNInferenceEngineInitialization() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val engine = CNNInferenceEngine(appContext)
        assertTrue("Model should be loaded successfully (or in prototype mode)", engine.isModelLoadedSuccessfully())
    }

    @Test
    fun testCNNInferenceEngineClassification() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val engine = CNNInferenceEngine(appContext)
        
        // Create a dummy green bitmap
        val bitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.GREEN)
        
        val results = engine.classifyLeaf(bitmap)
        
        assertNotNull(results)
        assertEquals(6, results.size)
        
        // Check if all 6 classes are present
        val classNames = results.map { it.className }
        assertTrue(classNames.contains("Bacterial Blight"))
        assertTrue(classNames.contains("Blast"))
        assertTrue(classNames.contains("Brown Spot"))
        assertTrue(classNames.contains("Tungro"))
        assertTrue(classNames.contains("Healthy"))
        assertTrue(classNames.contains("Out of Scope"))
        
        // Check if confidence scores are within valid range [0, 1]
        results.forEach { result ->
            assertTrue(result.confidence in 0f..1f)
        }
    }

    @Test
    fun testPreprocessingPipeline() {
        val pipeline = PreprocessingPipeline()
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.RED)
        
        val buffer = pipeline.preprocess(bitmap)
        
        assertNotNull(buffer)
        // 224 * 224 * 3 (RGB) = 150528 bytes
        assertEquals(224 * 224 * 3, buffer.capacity())
    }

    @Test
    fun testDatasetManagerMetadataAndSamples() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val datasetManager = DatasetManager(appContext)

        val metadata = datasetManager.getDatasetMetadata()
        assertNotNull("Dataset metadata should not be null", metadata)
        assertEquals("Rice Leaf Disease Combined Dataset", metadata?.datasetName)
        assertEquals(6588, metadata?.totalImages)
        assertEquals(5, metadata?.categories?.size)

        val samples = datasetManager.getAllSampleItems()
        assertFalse("Dataset sample items should not be empty", samples.isEmpty())
        assertTrue("At least 25 samples should be loaded across 5 categories", samples.size >= 25)

        // Verify sample bitmap classification with CNNInferenceEngine
        val engine = CNNInferenceEngine(appContext)
        val sampleItem = samples.first()
        val classificationResults = engine.classifyLeaf(sampleItem.bitmap)
        assertNotNull(classificationResults)
        assertTrue(classificationResults.isNotEmpty())
    }
}
