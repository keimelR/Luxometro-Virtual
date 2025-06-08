package com.example.luxometromovil.data.repository

import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.data.data_source.LightMeasurementDao
import com.example.luxometromovil.domain.model.database.LightMeasurementDetails
import com.example.luxometromovil.domain.model.database.relations.LightMeasurementWithDetails
import com.example.luxometromovil.domain.repository.LightMeasurementRepository
import kotlinx.coroutines.flow.Flow

class LightMeasurementRepositoryImpl(
    private val lightMeasurementDao: LightMeasurementDao
): LightMeasurementRepository {
    override fun getAllLightMeasurement(): Flow<List<LightMeasurement>>
    = lightMeasurementDao.getAllLightMeasurement()

    override fun getAllLightMeasurementWithDetails(): Flow<List<LightMeasurementWithDetails>>
    = lightMeasurementDao.getAllLightMeasurementWithDetails()

    override fun getLightMeasurementWithDetails(lightMeasurementId: Int): LightMeasurementWithDetails
    = lightMeasurementDao.getLightMeasurementWithDetails(lightMeasurementId)

    override suspend fun deleteLightMeasurement(lightMeasurementId: Int)
    = lightMeasurementDao.deleteLightMeasurement(lightMeasurementId)

    override suspend fun insertLightMeasurement(lightMeasurement: LightMeasurement)
    = lightMeasurementDao.insertLightMeasurement(lightMeasurement)

    override suspend fun insertLightMeasurementDetails(lightMeasurementDetails: LightMeasurementDetails)
    = lightMeasurementDao.insertLightMeasurementDetails(lightMeasurementDetails)

    override suspend fun updateTitleLightMeasurement(newTitle: String, id: Int)
    = lightMeasurementDao.updateTitleLightMeasurement(newTitle, id)
}