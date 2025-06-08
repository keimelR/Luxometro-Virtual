package com.example.luxometromovil.domain.use_case.light_history

data class LightMeasurementUseCases(
    val getAllLightMeasurementWithDetails: GetAllLightMeasurementWithDetails,
    val getAllLightMeasurement: GetAllLightMeasurement,
    val deleteLightMeasurement: DeleteLightMeasurement,
    val insertLightMeasurement: InsertLightMeasurement,
    val insertLightMeasurementDetails: InsertLightMeasurementDetails,
    val updateTitleLightMeasurement: UpdateTitleLightMeasurement
)
