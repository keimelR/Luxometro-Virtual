package com.example.luxometromovil.domain.use_case.scenes

import com.example.luxometromovil.domain.model.database.Scenes
import com.example.luxometromovil.domain.repository.ScenesRepository

class GetByIdScene(
    private val scenesRepository: ScenesRepository
) {
    suspend operator fun invoke(id: Int): Scenes {
        return scenesRepository.getByIdScene(id)
    }
}