package com.example.luxometromovil.presentation.luxometer_home

data class HomeUiState(
    val currentLux: Float = 0f,
    val currentFootCandle: Float = currentLux / 10.76f,
    val minMeasurementLight: Float = Float.MAX_VALUE,
    val maxMeasurementLight: Float = Float.MIN_VALUE
)