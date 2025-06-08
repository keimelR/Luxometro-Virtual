package com.example.luxometromovil.presentation.luxometer_list_scene

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxometromovil.R
import com.example.luxometromovil.domain.use_case.scenes.ScenesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListScenesViewModel @Inject constructor(
    private val scenesUseCases: ScenesUseCases
): ViewModel() {
    private val _uiState = mutableStateOf(ListSceneUiState())
    val uiState: State<ListSceneUiState> = _uiState

    init {
        getScenes()
    }

    fun onEvent(event: ListScenesEvent) {
        when (event) {
            is ListScenesEvent.GetListScenes -> {
                getScenes()
            }
        }
    }

    fun getImgScene(index: Int): Int {
        return when(index) {
            0 -> R.drawable.img_home
            1 -> R.drawable.img_comedor
            2 -> R.drawable.img_dormitorio
            3 -> R.drawable.img_oficina_hogar
            4 -> R.drawable.img_educational_center
            5 -> R.drawable.img_libreria
            else -> R.drawable.img_1280x720
        }
    }

    private fun getScenes() {
        scenesUseCases.getAllScenes()
            .onEach { scenes ->
                _uiState.value = uiState.value.copy(
                    scenes = scenes.map { scene ->
                        ScenesUi(
                            index = scene.id,
                            title = scene.title,
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
        Log.d("ListSceneViewModel", "Ejecutando getScenes")
    }
}
