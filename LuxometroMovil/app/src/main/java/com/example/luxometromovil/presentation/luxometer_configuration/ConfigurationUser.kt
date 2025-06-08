package com.example.luxometromovil.presentation.luxometer_configuration

import com.example.luxometromovil.domain.model.UnitMeasuringLight

data class ConfigurationUser(
    val darkTheme: Boolean = false,
    val unitMeasuringLight: UnitMeasuringLight = UnitMeasuringLight.LUX,
)