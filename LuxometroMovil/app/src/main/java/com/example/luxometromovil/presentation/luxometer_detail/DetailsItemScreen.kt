package com.example.luxometromovil.presentation.luxometer_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.luxometromovil.R

@Composable
fun DetailsItemScreen(
    detailsItemViewModel: DetailsItemViewModel = hiltViewModel(),
    index: Int,
) {
    val detailsItemUiState = detailsItemViewModel.uiState.collectAsStateWithLifecycle()

    detailsItemViewModel.onEvent(DetailsItemEvent.GetDetailItemById(index))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = detailsItemUiState.value.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${detailsItemUiState.value.minMeasuringLight} – ${detailsItemUiState.value.maxMeasuringLight}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = /*detailsItemViewModel.colorMeasuring*/ MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
            Text(
                text = "Lux",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        Text(
            text = detailsItemUiState.value.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showSystemUi = true, wallpaper = Wallpapers.NONE, device = "spec:parent=small_phone")
@Composable
fun DetailsItemLuxometerScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = "Estudios y Oficinas en Casa",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "400 – 750",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
            Text(
                text = "Lux",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 25.dp))

        Text(
            text = stringResource(R.string.studies_and_home_offices_description),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}