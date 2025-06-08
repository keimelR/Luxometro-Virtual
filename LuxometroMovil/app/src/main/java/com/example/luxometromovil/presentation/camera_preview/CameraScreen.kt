package com.example.luxometromovil.presentation.camera_preview

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import org.pytorch.IValue
import java.io.ByteArrayOutputStream
import org.pytorch.Module


@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var predictionResult by remember { mutableStateOf("Esperando predicción...") }

    // Cargar las etiquetas
    val labels = remember { loadLabels(context) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera(context, lifecycleOwner, previewView) { imageCapture = it }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera(context, lifecycleOwner, previewView) { imageCapture = it }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = predictionResult, fontSize = 18.sp, color = Color.White)

            Button(
                onClick = {
                    imageCapture?.let {
                        captureImageAsBitmap(context, it) { bitmap ->
                            predictionResult = classifyImage(context, bitmap)
                        }
                    } ?: Toast.makeText(context, "Cámara no inicializada", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Clasificar Lugar")
            }
        }
    }
}

private fun classifyImage(context: Context, bitmap: Bitmap): String {
    try {
        val labels = loadLabels(context) // Cargar etiquetas desde el archivo
        val module = Module.load(assetFilePath(context, "alexnet.ptl"))
        val inputTensor = preprocessBitmap(bitmap)
        val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()

        val scores = outputTensor.dataAsFloatArray

        // Limitamos la búsqueda a los primeros 365 índices
        val validScores = scores.take(labels.size)
        val predictedIndex = validScores.indices.maxByOrNull { validScores[it] } ?: -1

        // Obtener los 5 mejores resultados para depuración
        val top5Indices = validScores.indices.sortedByDescending { validScores[it] }.take(5)
        Log.d("PyTorch", "Top 5 índices: ${top5Indices.joinToString()}")

        return if (predictedIndex in labels.indices) {
            "Lugar: ${labels[predictedIndex]}"
        } else {
            "No se encontró una categoría válida"
        }
    } catch (e: Exception) {
        Log.e("PyTorch", "Error al clasificar la imagen", e)
        return "Error en clasificación: ${e.message}"
    }
}

// 4. Callback para ImageCapture
private fun startCamera(context: Context, lifecycleOwner: LifecycleOwner, previewView: PreviewView, onImageCaptureReady: (ImageCapture) -> Unit) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        val imageCapture = ImageCapture.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageCapture
            )
            // 5. Llamar al callback
            onImageCaptureReady(imageCapture)
        } catch (exc: Exception) {
            Log.e("CameraX", "Use case binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}

// 6. imageCapture como parámetro
private fun captureImage(context: Context, imageCapture: ImageCapture) {
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "photo_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(context, "Imagen guardada: ${photoFile.absolutePath}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
            }
        })
}

private fun captureImageAsBitmap(context: Context, imageCapture: ImageCapture, onBitmapReady: (Bitmap) -> Unit) {
    val outputStream = ByteArrayOutputStream()

    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val bitmap = imageProxyToBitmap(image)
            onBitmapReady(bitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
        }
    })
}

private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
private fun preprocessBitmap(bitmap: Bitmap): Tensor {
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
    val inputBuffer = TensorImageUtils.bitmapToFloat32Tensor(
        resizedBitmap,
        floatArrayOf(0.485f, 0.456f, 0.406f),  // Media de normalización
        floatArrayOf(0.229f, 0.224f, 0.225f)   // Desviación estándar
    )
    return inputBuffer
}

private fun assetFilePath(context: Context, assetName: String): String {
    val file = File(context.filesDir, assetName)
    if (!file.exists()) {
        context.assets.open(assetName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
    return file.absolutePath
}

private fun getLabel(index: Int): String {
    val labels = listOf("Playa", "Montaña", "Ciudad", "Bosque", "Desierto")
    return labels.getOrElse(index) { "Desconocido" }
}

private fun loadLabels(context: Context): List<String> {
    val labels = mutableListOf<String>()
    context.assets.open("IO_places365.txt").use { inputStream ->
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                labels.add(line.trim())
            }
        }
    }
    return labels
}
