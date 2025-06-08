package com.example.luxometromovil.domain.use_case.light_history

import com.example.luxometromovil.domain.model.database.LightMeasurementDetails
import com.example.luxometromovil.domain.repository.LightMeasurementRepository

class InsertLightMeasurementDetails(
    private val lightMeasurementRepository: LightMeasurementRepository
) {
    suspend operator fun invoke(lightMeasurementDetails: LightMeasurementDetails) {
        return lightMeasurementRepository.insertLightMeasurementDetails(lightMeasurementDetails)
    }
}