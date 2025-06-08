package com.example.luxometromovil.presentation.luxometer_configuration

import android.util.Log
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.MenuState
import com.composables.core.rememberMenuState
import com.example.luxometromovil.R
import com.example.luxometromovil.domain.model.UnitMeasuringLight
import com.example.luxometromovil.presentation.luxometer_configuration.components.DarkModeSwitch

@Composable
fun ConfigurationScreen(
    configurationViewModel: ConfigurationViewModel = hiltViewModel(),
    updateMeasuringLight: (UnitMeasuringLight) -> Unit,
    informationSensor: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    val configurationUiState by configurationViewModel.uiState.collectAsStateWithLifecycle()
    Log.d("ConfigurationScreen", configurationUiState.unitMeasuringLight.name)
    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.screen_options),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        DarkModeSwitch(
            darkTheme = configurationUiState.darkTheme,
            onThemeUpdate = { configurationViewModel.onEvent(ConfigurationEvent.TriggerDarkMode) }
        )

        UnitMeasuringSwitch(
            onSetMeasuringLight = {
                configurationViewModel.onEvent(ConfigurationEvent.ChooseUnitLightMeasurement(unitMeasuringLight = it))
                updateMeasuringLight(it)
            },
            unitMeasuringLight = configurationUiState.unitMeasuringLight
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
    }
}

@Composable
fun UnitMeasuringSwitch(
    onSetMeasuringLight: (UnitMeasuringLight) -> Unit,
    unitMeasuringLight: UnitMeasuringLight
) {
    val options = listOf(UnitMeasuringLight.LUX, UnitMeasuringLight.FC)
    val state = rememberMenuState(expanded = false)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Unidad de Medida",
            )

            MenuMeasuring(
                options = options,
                state = state,
                onSetMeasuringLight = onSetMeasuringLight,
                unitMeasuringLight = unitMeasuringLight
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
    }
}

@Composable
fun MenuMeasuring(
    options: List<UnitMeasuringLight>,
    onSetMeasuringLight: (UnitMeasuringLight) -> Unit,
    unitMeasuringLight: UnitMeasuringLight,
    state: MenuState
) {

    Column(Modifier.fillMaxWidth()) {
        Menu(state, modifier = Modifier.align(Alignment.End)) {
            MenuButton(
                Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(unitMeasuringLight.name)
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = if (!state.expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }

            MenuContent(
                modifier = Modifier
                    .width(320.dp)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                exit = fadeOut()
            ) {
                options.map { option ->
                    MenuItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp)),
                        onClick = {
                            onSetMeasuringLight(option)
                        }
                    ) {
                        Text(option.name, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp))
                    }
                }
            }
        }
    }
}


/*
@Composable
fun DisplayInformationLightSensor(
    informationSensor: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.title_information_sensor),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        informationSensor.map {
            when (it.key) {
                0 -> DataLightSensor("Nombre", it.value)
                1 -> DataLightSensor("Fabricante", it.value)
                2 -> DataLightSensor("Resolucion", it.value)
                3 -> DataLightSensor("Valor Maximo", it.value)
                else -> Text("")
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
}

@Composable
fun DataLightSensor(title: String, data: String) {
    Text(
        text = "$title: $data",
        style = MaterialTheme.typography.bodyMedium
    )
}

 */