package com.example.luxometromovil.presentation.luxometer_detail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxometromovil.domain.model.UnitMeasuringLight
import com.example.luxometromovil.domain.use_case.scenes.ScenesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsItemViewModel @Inject constructor(
    private val scenesUseCases: ScenesUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailsItemSceneUiState())
    val uiState: StateFlow<DetailsItemSceneUiState> = _uiState.asStateFlow()

    var colorMeasuring: Color = Color(0xFF000000)

    fun onEvent(event: DetailsItemEvent) {
        when (event) {
            is DetailsItemEvent.GetDetailItemById -> {
                getDetailsItem(event.id)
            }
        }
    }

     fun changeColorMeasuring(
         unitMeasuringLight: UnitMeasuringLight = UnitMeasuringLight.LUX,
         currentMeasuringLight: Float
    ) {
        when(unitMeasuringLight) {
            UnitMeasuringLight.LUX -> {
                colorMeasuring = when {
                    (currentMeasuringLight >= _uiState.value.minMeasuringLight) && (currentMeasuringLight <= _uiState.value.maxMeasuringLight) -> Color(0xFF4CAF50)

                    ((currentMeasuringLight >= (75 * _uiState.value.minMeasuringLight / 100f))
                            && currentMeasuringLight < _uiState.value.minMeasuringLight)
                            || (currentMeasuringLight > _uiState.value.maxMeasuringLight
                            && (currentMeasuringLight <= 125 * _uiState.value.maxMeasuringLight / 100f)) -> Color(0xFFFFCC00)

                    else -> Color(0xFFE91E1E)
                }
            }
            UnitMeasuringLight.FC -> {

            }
        }
    }

    private fun getDetailsItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            scenesUseCases.getByIdScene(id).let { scene ->
                _uiState.update {
                    it.copy(
                        id = scene.id,
                        title = scene.title,
                        minMeasuringLight = scene.minMeasuringLight,
                        maxMeasuringLight = scene.maxMeasuringLight,
                        description = scene.description
                    )
                }
            }
        }
    }
}