package com.example.luxometromovil.presentation.luxometer_home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.luxometromovil.R
import com.example.luxometromovil.domain.model.UnitMeasuringLight
import com.example.luxometromovil.presentation.luxometer_home.components.EventButton
import com.example.luxometromovil.presentation.luxometer_home.components.StatsLuxometer
import org.pytorch.Module

private lateinit var alexnet: Module

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onStartCamera: () -> Unit,
    unitMeasuringLight: UnitMeasuringLight = UnitMeasuringLight.LUX,
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current // Obtener el contexto


    Log.d("HomeScreen", unitMeasuringLight.name)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopAppBar(
            onStartCamera = onStartCamera
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "El Actual Nivel de Iluminacion",
                fontStyle = FontStyle.Italic
            )
            HomeLuxometerMeasuringLight(
                measuringLight = when (unitMeasuringLight) {
                    UnitMeasuringLight.LUX -> homeUiState.currentLux
                    UnitMeasuringLight.FC -> homeUiState.currentFootCandle
                },
                titleUnitMeasuringLight = when (unitMeasuringLight) {
                    UnitMeasuringLight.LUX -> R.string.unit_lux
                    UnitMeasuringLight.FC -> R.string.unit_foot_candle
                },
                modifier = Modifier.fillMaxWidth()
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ResetButton(
                    resetClick = { homeViewModel.onEvent(HomeLightEvent.ResetMeasurement) },
                    isEmptyListMeasurementLight = homeViewModel.isEmptyListMeasurementLight(),
                    modifier = Modifier.weight(0.5f)
                )
                Spacer(Modifier.padding(horizontal = 6.dp))
                EventButton(
                    isSensorActive = homeViewModel.getIsSensorActive(),
                    onStartClick = { homeViewModel.onEvent(HomeLightEvent.StartSensor) },
                    onStopClick = { homeViewModel.onEvent(HomeLightEvent.StopSensor) },
                    modifier = Modifier.weight(0.5f)
                )
            }
        }

        StatsLuxometer(
            minMeasuringLight = homeUiState.minMeasurementLight,
            maxMeasuringLight = homeUiState.maxMeasurementLight,
            unitMeasuringLight = unitMeasuringLight,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun ResetButton(
    resetClick: () -> Unit,
    isEmptyListMeasurementLight: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { resetClick() },
        enabled = !isEmptyListMeasurementLight,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null
            )

            Text("Reestablecer")
        }
    }
}

@Composable
fun InfoOfUseLuxometer() {
    var openAlertDialog by remember { mutableStateOf(false) }

    IconButton (
        onClick = {openAlertDialog = true},
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }

    if (openAlertDialog) {
        Dialog(
            onDismissRequest = {
                openAlertDialog = false
            }
        ) {
            Card(
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    stringResource(R.string.tutorial_luxometer),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HomeLuxometerMeasuringLight(
    measuringLight: Float,
    titleUnitMeasuringLight: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {

        Text(
            text = stringResource(titleUnitMeasuringLight) + " " + "%.2f".format(measuringLight),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun TopAppBar(
    onStartCamera: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Takephoto(onStartCamera)
            InfoOfUseLuxometer()
        }
    }
}

@Composable
fun Takephoto(
    onStartCamera: () -> Unit
) {
    IconButton(
        onClick = { onStartCamera() }
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_photo_camera_24),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}



@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 80.dp)
    ) {
        Text(
            text = "Detalles Captados",
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.padding(vertical = 6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Iluminacion Maxima: ")
            Text(
                "Lux 12.00",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.padding(vertical = 6.dp))
        HorizontalDivider()
        Spacer(Modifier.padding(vertical = 6.dp))

        Column(
            Modifier.fillMaxWidth()
        ) {

            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Iluminacion Minima: ")
                    Text(
                        "Lux 2.00",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Icon(imageVector = Icons.Filled.Info, contentDescription = "")
            }
        }
    }
}