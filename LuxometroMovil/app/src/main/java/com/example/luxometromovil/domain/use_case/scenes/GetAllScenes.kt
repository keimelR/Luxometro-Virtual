package com.example.luxometromovil.domain.use_case.scenes

import com.example.luxometromovil.domain.model.database.Scenes
import com.example.luxometromovil.domain.repository.ScenesRepository
import kotlinx.coroutines.flow.Flow

class GetAllScenes(
    private val scenesRepository: ScenesRepository
) {
    operator fun invoke(): Flow<List<Scenes>> {
        return scenesRepository.getAllScenes()
    }
}