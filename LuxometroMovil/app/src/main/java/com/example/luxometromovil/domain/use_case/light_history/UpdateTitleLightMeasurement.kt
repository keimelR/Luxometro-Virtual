package com.example.luxometromovil.domain.use_case.light_history

import com.example.luxometromovil.domain.repository.LightMeasurementRepository

class UpdateTitleLightMeasurement(
    private val lightMeasurementRepository: LightMeasurementRepository
) {
    suspend operator fun invoke(newTitle: String, id: Int) {
        return lightMeasurementRepository.updateTitleLightMeasurement(newTitle, id)
    }
}