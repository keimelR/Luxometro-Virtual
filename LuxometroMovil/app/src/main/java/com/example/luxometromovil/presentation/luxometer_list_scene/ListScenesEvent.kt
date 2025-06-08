package com.example.luxometromovil.presentation.luxometer_list_scene

sealed class ListScenesEvent {
    data class GetListScenes(val listSceneUiState: ListSceneUiState): ListScenesEvent()
}