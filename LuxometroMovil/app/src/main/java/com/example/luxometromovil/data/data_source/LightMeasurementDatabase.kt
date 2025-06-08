package com.example.luxometromovil.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.model.database.LightMeasurementDetails

@Database(
    entities = [LightMeasurement::class, LightMeasurementDetails::class],
    version = 1,
    exportSchema = false
)
abstract class LightMeasurementDatabase: RoomDatabase() {
    abstract val lightMeasurementDao: LightMeasurementDao

    companion object {
        const val DATABASE_NAME = "light_measurement_db"
    }
}