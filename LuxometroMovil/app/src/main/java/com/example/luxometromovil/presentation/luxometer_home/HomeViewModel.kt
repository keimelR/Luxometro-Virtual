package com.example.luxometromovil.presentation.luxometer_home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxometromovil.data.sensor.LightSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private  val lightSensor: LightSensor
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val measuringLight: MutableList<Float> = mutableListOf()
    private var isSensorActive = mutableStateOf(false)

    fun onEvent(event: HomeLightEvent) {
        when(event) {
            is HomeLightEvent.StartSensor -> {
                onSensorStart()
            }
            is HomeLightEvent.StopSensor -> {
                onSensorStop()
            }
            HomeLightEvent.ResetMeasurement -> {
                resetMeasurement()
            }
        }
    }


    fun isEmptyListMeasurementLight(): Boolean = measuringLight.isEmpty()

    private fun resetMeasurement() {
        if (measuringLight.isEmpty()) {
            return
        }
        measuringLight.clear()
        resetValues()
    }

    private fun resetValues() {
        _uiState.update {
            it.copy(
                currentLux = 0f,
                currentFootCandle = 0f,
                minMeasurementLight = Float.MAX_VALUE,
                maxMeasurementLight = Float.MIN_VALUE
            )
        }
    }

    private fun observeLightSensor() {
        viewModelScope.launch {
            lightSensor.currentLux.collect { newLux ->
                if (newLux == -1f && measuringLight.isEmpty()) {
                    return@collect
                }
                measuringLight.add(newLux)

                val minLight = measuringLight.minOrNull() ?: Float.MAX_VALUE
                val maxLight = measuringLight.maxOrNull() ?: Float.MIN_VALUE
                _uiState.update {
                    it.copy(
                        currentLux = newLux,
                        currentFootCandle = newLux / 10.76f,
                        minMeasurementLight = minLight,
                        maxMeasurementLight = maxLight,
                    )
                }
            }
        }
    }

    private fun setIsSensorActive() {
        isSensorActive.value = !isSensorActive.value
    }

    fun getIsSensorActive(): Boolean = isSensorActive.value

    private fun onSensorStart() {
        lightSensor.onStartListening()
        setIsSensorActive()
        observeLightSensor()
    }

    private fun onSensorStop() {
        setIsSensorActive()
        lightSensor.onStopListening()
    }
}