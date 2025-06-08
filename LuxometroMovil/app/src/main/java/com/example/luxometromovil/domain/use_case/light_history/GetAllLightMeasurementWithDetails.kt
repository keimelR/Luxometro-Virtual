package com.example.luxometromovil.domain.use_case.light_history

import com.example.luxometromovil.domain.model.database.relations.LightMeasurementWithDetails
import com.example.luxometromovil.domain.repository.LightMeasurementRepository
import kotlinx.coroutines.flow.Flow

class GetAllLightMeasurementWithDetails(
    private val lightMeasurementRepository: LightMeasurementRepository
) {
    operator fun invoke(): Flow<List<LightMeasurementWithDetails>> {
        return lightMeasurementRepository.getAllLightMeasurementWithDetails()
    }
}