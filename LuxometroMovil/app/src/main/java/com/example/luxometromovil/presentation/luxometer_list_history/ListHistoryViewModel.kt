package com.example.luxometromovil.presentation.luxometer_list_history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxometromovil.domain.model.database.LightMeasurement
import com.example.luxometromovil.domain.model.database.toHistoryUi
import com.example.luxometromovil.domain.use_case.light_history.LightMeasurementUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ListHistoryViewModel @Inject constructor(
    private val lightMeasurementUseCases: LightMeasurementUseCases
): ViewModel() {
    private val _uiState = MutableStateFlow(ListHistoryUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ListHistoryEvent) {
        when(event) {
            is ListHistoryEvent.AddNewItemHistory -> {
                addNewItemHistory(
                    title = event.title,
                    unitMeasuringLight = event.unitMeasuringLight.name
                )
            }
            is ListHistoryEvent.GetByIdItemHistory -> {

            }
            is ListHistoryEvent.GetAllItemHistory -> {
                getAllItemHistory()
                Log.d(
                    "ListHistoryViewModel",
                    "Ejecutando evento GetAllItemHistory"
                )
            }
            is ListHistoryEvent.DeleteItemHistory -> {
                deleteItemHistory(event.id)
                Log.d(
                    "ListHistoryViewModel",
                    "Ejecutando evento DeleteItemHistory, del item ${event.id}"
                )
            }
            is ListHistoryEvent.UpdateTitleItemHistory -> {
                updateTitleItemHistory(
                    newTitle = event.newTitle,
                    id = event.id
                )
                Log.d(
                    "ListHistoryViewModel",
                    "Ejecutando evento UpdateTitleItemHistory, del item ${event.id} a un nuevo nombre de ${event.newTitle}"
                )
            }
        }
    }

    private fun updateTitleItemHistory(newTitle: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            lightMeasurementUseCases.updateTitleLightMeasurement(newTitle, id)
        }
    }

    private fun deleteItemHistory(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            lightMeasurementUseCases.deleteLightMeasurement(id)
        }
    }

    private fun addNewItemHistory(title: String, unitMeasuringLight: String) {
        viewModelScope.launch(Dispatchers.IO) {
            lightMeasurementUseCases.insertLightMeasurement(
                lightMeasurement = LightMeasurement(
                    title = title,
                    unitMeasuringLight = unitMeasuringLight,
                    createdAt = "${DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")}"
                )
            )
        }
    }

    private fun getAllItemHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            lightMeasurementUseCases.getAllLightMeasurement()
                .flowOn(Dispatchers.IO)
                .map { listMeasurement ->
                    listMeasurement.map { item ->
                        item.toHistoryUi(
                            id = item.id,
                            title = item.title,
                            unitMeasuringLight = item.unitMeasuringLight
                        )
                    }
                }
                .collect { historyUiList ->
                    _uiState.update {
                        it.copy(
                            itemsHistoryUi = historyUiList
                        )
                    }
                }
        }
    }
}