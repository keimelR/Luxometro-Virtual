package com.example.luxometromovil.domain.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.luxometromovil.presentation.luxometer_list_history.HistoryUi

@Entity(tableName = "light_measurement")
data class LightMeasurement (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "unit_measuring_light") val unitMeasuringLight: String,
    @ColumnInfo(name = "created_at") val createdAt: String
)

fun LightMeasurement.toHistoryUi(
    id: Int,
    title: String,
    unitMeasuringLight: String,
): HistoryUi {
    return HistoryUi(id, title, unitMeasuringLight)
}