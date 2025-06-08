package com.example.luxometromovil.presentation.share_viewModel

import androidx.lifecycle.ViewModel
import com.example.luxometromovil.data.util.DataStoreUtil
import com.example.luxometromovil.domain.model.UnitMeasuringLight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dataStoreUtil: DataStoreUtil
): ViewModel() {
    private val _unitMeasuringLight = MutableStateFlow(UnitMeasuringLight.LUX)
    val unitMeasuringLight = _unitMeasuringLight.asStateFlow()

    fun updateUnitMeasuringLight(unitMeasuringLight: UnitMeasuringLight) {
        _unitMeasuringLight.update {
            unitMeasuringLight
        }
    }
}