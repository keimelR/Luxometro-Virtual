package com.example.luxometromovil.presentation.luxometer_configuration

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxometromovil.data.util.DataStoreUtil
import com.example.luxometromovil.data.util.DataStoreUtil.Companion.IS_DARK_MODE_KEY
import com.example.luxometromovil.domain.model.UnitMeasuringLight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val _uiState: MutableStateFlow<ConfigurationUser> = MutableStateFlow(ConfigurationUser())
    val uiState: StateFlow<ConfigurationUser> = _uiState.asStateFlow()

    private val dataStore = dataStoreUtil.dataStore

    fun onEvent(event: ConfigurationEvent) {
        when(event) {
            is ConfigurationEvent.ChooseUnitLightMeasurement -> {
                setUnitMeasuring(event.unitMeasuringLight)
            }
            is ConfigurationEvent.TriggerDarkMode -> {
                onThemeUpdate()
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.data.map { preferences ->
                preferences[IS_DARK_MODE_KEY] ?: false
            }.collect {
                _uiState.update { configurationUser ->
                    configurationUser.copy(
                        darkTheme = it
                    )
                }
            }
        }
    }

    val unitMeasuringLight: StateFlow<UnitMeasuringLight> =
        uiState.map { it.unitMeasuringLight }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            UnitMeasuringLight.LUX
        )

    private fun onThemeUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[IS_DARK_MODE_KEY] = !(preferences[IS_DARK_MODE_KEY] ?: false)
            }
        }
    }

    private fun setUnitMeasuring(unitMeasuringLight: UnitMeasuringLight) {
        _uiState.update {
            it.copy(
                unitMeasuringLight = unitMeasuringLight
            )
        }
    }
}