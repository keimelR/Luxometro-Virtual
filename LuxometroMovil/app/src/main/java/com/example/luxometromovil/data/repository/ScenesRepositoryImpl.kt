package com.example.luxometromovil.data.repository

import com.example.luxometromovil.data.data_source.ScenesDao
import com.example.luxometromovil.domain.model.database.Scenes
import com.example.luxometromovil.domain.repository.ScenesRepository
import kotlinx.coroutines.flow.Flow

class ScenesRepositoryImpl(
    private val scenesDao: ScenesDao
): ScenesRepository {
    override fun getAllScenes(): Flow<List<Scenes>> = scenesDao.getAllScenes()
    override suspend fun getByIdScene(id: Int): Scenes = scenesDao.getByIdScene(id)

    override suspend fun insertDefaultScenes(scenes: Scenes) = scenesDao.insertDefaultScenes(scenes)
}