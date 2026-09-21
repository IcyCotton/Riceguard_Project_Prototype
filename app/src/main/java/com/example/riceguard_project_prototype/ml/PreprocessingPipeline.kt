package com.example.riceguard_project_prototype.ml

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PreprocessingPipeline {
    companion object {
        const val INPUT_SIZE = 224
    }

    /**
     * Preprocesses a Bitmap image into a ByteBuffer format required by the TFLite model.
     * Scales the bitmap to 224x224 and extracts RGB channel values.
     */
    fun preprocess(bitmap: Bitmap): ByteBuffer {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        // 224 * 224 * 3 channels (RGB) * 1 byte per channel for quantized model
        val byteBuffer = ByteBuffer.allocateDirect(INPUT_SIZE * INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        for (pixelValue in intValues) {
            val r = (pixelValue shr 16) and 0xFF
            val g = (pixelValue shr 8) and 0xFF
            val b = pixelValue and 0xFF

            byteBuffer.put(r.toByte())
            byteBuffer.put(g.toByte())
            byteBuffer.put(b.toByte())
        }
        return byteBuffer
    }
}
