package com.example.luxometromovil.domain.repository

import com.example.luxometromovil.domain.model.database.Scenes
import kotlinx.coroutines.flow.Flow

interface ScenesRepository {
    fun getAllScenes(): Flow<List<Scenes>>
    suspend fun getByIdScene(id: Int): Scenes
    suspend fun insertDefaultScenes(scenes: Scenes)
}