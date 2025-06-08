package com.example.luxometromovil.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.luxometromovil.domain.model.database.Scenes

@Database(
    entities = [Scenes::class],
    version = 1,
    exportSchema = false
)
abstract class ScenesDatabase: RoomDatabase() {
    abstract val scenesDao: ScenesDao

    companion object {
        const val DATABASE_NAME = "scenes_db"
    }
}