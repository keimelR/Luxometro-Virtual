package com.example.luxometromovil.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.model.database.LightMeasurementDetails
import com.example.luxometromovil.domain.model.database.relations.LightMeasurementWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
@RewriteQueriesToDropUnusedColumns
interface LightMeasurementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLightMeasurement(lightMeasurement: LightMeasurement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLightMeasurementDetails(lightMeasurementDetails: LightMeasurementDetails)

    @Query("""
        Update light_measurement
        Set title = :newTitle
        Where light_measurement.id = :id
    """)
    suspend fun updateTitleLightMeasurement(newTitle: String, id: Int)

    @Update
    suspend fun updateLightMeasurementDetails(details: LightMeasurementDetails)

    @Query("""Delete From light_measurement Where light_measurement.id = :lightMeasurementId""")
    suspend fun deleteLightMeasurement(lightMeasurementId: Int)

    @Transaction
    @Query("""
        Select * From light_measurement
        Inner Join light_measurement_details
        On light_measurement.id = light_measurement_details.light_measurement_id
    """)
    fun getAllLightMeasurementWithDetails(): Flow<List<LightMeasurementWithDetails>>

    @Transaction
    @Query("""
        Select * From light_measurement
        Inner Join light_measurement_details
        On light_measurement.id = light_measurement_details.light_measurement_id
        Where light_measurement.id = :measurementLightId
    """)
    fun getLightMeasurementWithDetails(measurementLightId: Int): LightMeasurementWithDetails

    @Query("Select * From light_measurement")
    fun getAllLightMeasurement(): Flow<List<LightMeasurement>>
}