package com.example.luxometromovil.presentation.luxometer_list_scene

data class ListSceneUiState(
    val scenes: List<ScenesUi> = emptyList()
)

data class ScenesUi(
    val index: Int = 0,
    val title: String = "",
)
