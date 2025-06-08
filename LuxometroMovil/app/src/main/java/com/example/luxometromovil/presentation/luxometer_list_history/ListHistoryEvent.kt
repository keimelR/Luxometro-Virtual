package com.example.luxometromovil.presentation.luxometer_list_history

import com.example.luxometromovil.domain.model.UnitMeasuringLight

sealed class ListHistoryEvent {
    data class AddNewItemHistory(val title: String, val unitMeasuringLight: UnitMeasuringLight): ListHistoryEvent()
    data class GetByIdItemHistory(val id: Int): ListHistoryEvent()
    object GetAllItemHistory: ListHistoryEvent()
    data class DeleteItemHistory(val id: Int): ListHistoryEvent()
    data class UpdateTitleItemHistory(val newTitle: String, val id: Int): ListHistoryEvent()
}