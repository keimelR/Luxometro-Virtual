package com.example.luxometromovil.domain.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenes")
data class Scenes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "min_measuring_light") val minMeasuringLight: Double,
    @ColumnInfo(name = "max_measuring_light") val maxMeasuringLight: Double,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "created_at") val createdAt: String
)