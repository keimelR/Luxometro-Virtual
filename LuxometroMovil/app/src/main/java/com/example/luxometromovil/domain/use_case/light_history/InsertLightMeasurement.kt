package com.example.luxometromovil.domain.use_case.light_history

import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.repository.LightMeasurementRepository

class InsertLightMeasurement(
    private val lightMeasurementRepository: LightMeasurementRepository
) {
    suspend operator fun invoke(lightMeasurement: LightMeasurement) {
        return lightMeasurementRepository.insertLightMeasurement(lightMeasurement)
    }
}