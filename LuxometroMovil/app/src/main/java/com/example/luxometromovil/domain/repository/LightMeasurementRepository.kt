package com.example.luxometromovil.domain.repository

import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.model.database.LightMeasurementDetails
import com.example.luxometromovil.domain.model.database.relations.LightMeasurementWithDetails
import kotlinx.coroutines.flow.Flow

interface LightMeasurementRepository {
    fun getAllLightMeasurement(): Flow<List<LightMeasurement>>
    fun getAllLightMeasurementWithDetails(): Flow<List<LightMeasurementWithDetails>>
    fun getLightMeasurementWithDetails(lightMeasurementId: Int): LightMeasurementWithDetails

    suspend fun deleteLightMeasurement(lightMeasurementId: Int)

    suspend fun insertLightMeasurement(lightMeasurement: LightMeasurement)
    suspend fun insertLightMeasurementDetails(lightMeasurementDetails: LightMeasurementDetails)

    suspend fun updateTitleLightMeasurement(newTitle: String, id: Int)
}