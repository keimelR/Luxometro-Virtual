package com.example.luxometromovil.presentation.luxometer_home

sealed class HomeLightEvent {
    object StartSensor: HomeLightEvent()
    object StopSensor: HomeLightEvent()
    object ResetMeasurement: HomeLightEvent()
}