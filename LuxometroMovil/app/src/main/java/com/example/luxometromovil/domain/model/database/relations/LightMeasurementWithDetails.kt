package com.example.luxometromovil.domain.model.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.model.database.LightMeasurementDetails

data class LightMeasurementWithDetails(
    @Embedded val lightMeasurement: LightMeasurement,
    @Relation(
        parentColumn = "id",
        entityColumn = "light_measurement_id"
    )
    val measurementValues: List<LightMeasurementDetails>
)
