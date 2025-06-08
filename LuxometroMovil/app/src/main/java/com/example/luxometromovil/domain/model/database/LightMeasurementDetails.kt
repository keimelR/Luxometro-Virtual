package com.example.luxometromovil.domain.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "light_measurement_details")
data class LightMeasurementDetails(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "light_measurement_id") val measurementLightId: Int,
    @ColumnInfo(name = "measurement_value") val measurementValue: Double,
    @ColumnInfo(name = "created_at") val createdAt: Float
)