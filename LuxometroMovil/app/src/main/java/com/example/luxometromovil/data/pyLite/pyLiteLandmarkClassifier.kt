package com.example.luxometromovil.data.pyLite

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import com.example.luxometromovil.domain.model.classification.Classification
import com.example.luxometromovil.domain.model.classification.LandmarkClassifier

class pyLiteLandmarkClassifier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResults: Int = 1
): LandmarkClassifier {
    private var imageAnalysis: ImageAnalysis? = null

    private fun setupClassifier() {
    }

    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        return listOf(
            Classification(
                "",
                1.4f
            )
        )
    }
}