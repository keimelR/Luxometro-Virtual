package com.example.luxometromovil.presentation.luxometer_list_history

data class ListHistoryUiState(
    val itemsHistoryUi: List<HistoryUi> = emptyList()
)

data class HistoryUi(
    val id: Int = 0,
    val title: String = "",
    val unitMeasuringLight: String = "",
    val minMeasuringLight: Double = 0.00,
    val maxMeasuringLight: Double = 0.00,
    val avgMeasuringLight: Double = 0.00,
)