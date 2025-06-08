package com.example.luxometromovil.domain.use_case.light_history

import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.repository.LightMeasurementRepository
import kotlinx.coroutines.flow.Flow

class GetAllLightMeasurement(
    private val lightMeasurementRepository: LightMeasurementRepository
) {
    operator fun invoke(): Flow<List<LightMeasurement>> {
        return lightMeasurementRepository.getAllLightMeasurement()
    }
}