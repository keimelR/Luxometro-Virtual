package com.example.luxometromovil.presentation.camera_preview


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
//import com.google.ai.client.generativeai.type.image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Replace with your actual API key
const val GEMINI_API_KEY = "AIzaSyBTBxscBbYcKPKj3XWqXmuxXc2r6L25H10"

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    // State for the lux estimate
    var detectedLuxResult by remember { mutableStateOf("No lux detected yet.") }
    // State for the raw Gemini response
    var rawGeminiResponse by remember { mutableStateOf("No raw Gemini response yet.") }


    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                Toast.makeText(context, "Camera permissions granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permissions not granted. Camera won't work.", Toast.LENGTH_LONG).show()
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                photoUri?.let { uri ->
                    Toast.makeText(context, "Photo taken successfully.", Toast.LENGTH_SHORT).show()
                    coroutineScope.launch(Dispatchers.IO) {
                        val bitmap = uriToBitmap(context.contentResolver, uri)

                        if (bitmap != null) {
                            analyzeImageWithGemini(context, bitmap) { luxResult, rawResponse ->
                                coroutineScope.launch(Dispatchers.Main) {
                                    detectedLuxResult = luxResult
                                    rawGeminiResponse = rawResponse // Update the raw response state
                                    Toast.makeText(context, "Analysis complete!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Log.e("CameraScreen", "Could not convert URI to Bitmap")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error processing image.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Photo capture canceled or failed.", Toast.LENGTH_SHORT).show()
                photoUri = null
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val requiredPermissions = mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

            val allGranted = requiredPermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

            if (allGranted) {
                val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/LuxometerApp")
                    }
                }
                photoUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                photoUri?.let {
                    takePictureLauncher.launch(it)
                } ?: run {
                    Toast.makeText(context, "Error creating file for photo.", Toast.LENGTH_SHORT).show()
                    Log.e("CameraScreen", "Error: photoUri is null when trying to create file.")
                }
            } else {
                requestPermissionLauncher.launch(requiredPermissions)
            }
        }) {
            Text("Take Photo and Analyze Lux")
        }

        // Display both the parsed estimate and the raw response
        Text(
            text = "Lux Estimado: $detectedLuxResult",
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Ai respuesta: $rawGeminiResponse",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

fun uriToBitmap(contentResolver: android.content.ContentResolver, selectedFileUri: Uri): Bitmap? {
    return try {
        contentResolver.openInputStream(selectedFileUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        Log.e("CameraScreen", "Error converting URI to Bitmap: ${e.message}")
        null
    }
}

// Function to analyze the image using Gemini API
suspend fun analyzeImageWithGemini(context: Context, bitmap: Bitmap, onResult: (luxEstimate: String, rawResponse: String) -> Unit) {
    val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-pro",
        apiKey = GEMINI_API_KEY
    )

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, (bitmap.height * (640.0 / bitmap.width)).toInt(), false)

    try {
        val response = generativeModel.generateContent(
            content {
                image(scaledBitmap)
                text("Analiza las condiciones de iluminación en esta imagen. Calcula el nivel aproximado de iluminancia en lux. Si es posible, proporciona una estimación numérica o descríbela cualitativamente (p. ej., «oficina bien iluminada», «luz tenue al atardecer»), acorta la respuesta, y no me pongas ningun encabezado como claro o por supuesto como una IA")
            }
        )

        val resultText = response.text ?: "No textual response from Gemini."
        Log.d("CameraScreen", "Gemini Response: $resultText")

        val luxValue = parseLuxFromResult(resultText)

        // Pass both values to the callback
        onResult(luxValue, resultText)

    } catch (e: Exception) {
        Log.e("CameraScreen", "Gemini API call failed: ${e.message}", e)
        // Pass a default error message for both values
        onResult("Error: ${e.message}", "Error during Gemini analysis: ${e.message}")
    }
}

// Helper function to try and parse a numerical lux value from Gemini's response
fun parseLuxFromResult(geminiResponse: String): String {
    val regex = "(\\d+\\.?\\d*)\\s*(lux|lx)".toRegex(RegexOption.IGNORE_CASE)
    val match = regex.find(geminiResponse)

    // If a number is found, return it. Otherwise, return the original response prefixed.
    return match?.groupValues?.get(1) ?: "Estimación de Gemini: $geminiResponse"
}