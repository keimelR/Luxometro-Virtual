package com.example.luxometromovil.presentation.luxometer_configuration

import com.example.luxometromovil.domain.model.UnitMeasuringLight

sealed class ConfigurationEvent {
    object TriggerDarkMode: ConfigurationEvent()
    data class ChooseUnitLightMeasurement(val unitMeasuringLight: UnitMeasuringLight): ConfigurationEvent()
}