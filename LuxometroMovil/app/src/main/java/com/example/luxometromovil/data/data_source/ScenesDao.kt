package com.example.luxometromovil.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.luxometromovil.domain.model.database.Scenes
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenesDao {
    @Query("""Select * From scenes""")
    fun getAllScenes(): Flow<List<Scenes>>

    @Query("""
        Select * From scenes
        Where scenes.id = :id
    """)
    suspend fun getByIdScene(id: Int): Scenes

    @Insert
    suspend fun insertDefaultScenes(scenes: Scenes)
}