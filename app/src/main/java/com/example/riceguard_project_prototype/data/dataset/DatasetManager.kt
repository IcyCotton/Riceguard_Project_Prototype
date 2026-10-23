package com.example.riceguard_project_prototype.data.dataset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.json.JSONObject
import java.io.InputStream

data class DatasetCategoryInfo(
    val id: String,
    val displayName: String,
    val imageCount: Int,
    val description: String,
    val sampleFolder: String
)

data class DatasetMetadata(
    val datasetName: String,
    val version: String,
    val totalImages: Int,
    val description: String,
    val categories: List<DatasetCategoryInfo>
)

data class DatasetSampleItem(
    val categoryId: String,
    val categoryDisplayName: String,
    val assetPath: String,
    val bitmap: Bitmap
)

class DatasetManager(private val context: Context) {

    private var metadata: DatasetMetadata? = null

    init {
        loadMetadata()
    }

    /**
     * Loads and parses the dataset_metadata.json from Android assets.
     */
    private fun loadMetadata() {
        try {
            val jsonString = context.assets.open("dataset/dataset_metadata.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonObject = JSONObject(jsonString)
            val datasetName = jsonObject.optString("dataset_name", "Rice Leaf Dataset")
            val version = jsonObject.optString("version", "1.0")
            val totalImages = jsonObject.optInt("total_images", 6588)
            val description = jsonObject.optString("description", "")

            val jsonArray = jsonObject.getJSONArray("classes")
            val categoriesList = mutableListOf<DatasetCategoryInfo>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                categoriesList.add(
                    DatasetCategoryInfo(
                        id = item.getString("id"),
                        displayName = item.getString("display_name"),
                        imageCount = item.getInt("image_count"),
                        description = item.getString("description"),
                        sampleFolder = item.getString("sample_folder")
                    )
                )
            }

            metadata = DatasetMetadata(
                datasetName = datasetName,
                version = version,
                totalImages = totalImages,
                description = description,
                categories = categoriesList
            )
            Log.d("DatasetManager", "Dataset metadata loaded successfully: $totalImages images across ${categoriesList.size} classes.")
        } catch (e: Exception) {
            Log.e("DatasetManager", "Failed to load dataset metadata from assets", e)
        }
    }

    fun getDatasetMetadata(): DatasetMetadata? = metadata

    /**
     * Loads sample Bitmaps for a given category from assets.
     */
    fun getSampleBitmapsForCategory(categoryId: String): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        try {
            val folderPath = "dataset/sample_images/$categoryId"
            val files = context.assets.list(folderPath) ?: emptyArray()

            for (fileName in files) {
                val inputStream: InputStream = context.assets.open("$folderPath/$fileName")
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    bitmaps.add(bitmap)
                }
                inputStream.close()
            }
        } catch (e: Exception) {
            Log.e("DatasetManager", "Error loading sample bitmaps for category: $categoryId", e)
        }
        return bitmaps
    }

    /**
     * Gets all available sample items across categories bundled in assets.
     */
    fun getAllSampleItems(): List<DatasetSampleItem> {
        val sampleItems = mutableListOf<DatasetSampleItem>()
        val categories = metadata?.categories ?: return emptyList()

        for (category in categories) {
            val folderPath = "dataset/sample_images/${category.id}"
            try {
                val files = context.assets.list(folderPath) ?: emptyArray()
                for (fileName in files) {
                    val assetPath = "$folderPath/$fileName"
                    val inputStream = context.assets.open(assetPath)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        sampleItems.add(
                            DatasetSampleItem(
                                categoryId = category.id,
                                categoryDisplayName = category.displayName,
                                assetPath = assetPath,
                                bitmap = bitmap
                            )
                        )
                    }
                    inputStream.close()
                }
            } catch (e: Exception) {
                Log.e("DatasetManager", "Error reading asset folder: $folderPath", e)
            }
        }
        return sampleItems
    }

    /**
     * Selects a random sample image from a given or random category for classification testing.
     */
    fun getRandomSampleItem(preferredCategory: String? = null): DatasetSampleItem? {
        val allSamples = getAllSampleItems()
        if (allSamples.isEmpty()) return null

        val filtered = if (!preferredCategory.isNullOrBlank()) {
            allSamples.filter { it.categoryId.equals(preferredCategory, ignoreCase = true) }
        } else {
            allSamples
        }

        return (if (filtered.isNotEmpty()) filtered else allSamples).randomOrNull()
    }
}
