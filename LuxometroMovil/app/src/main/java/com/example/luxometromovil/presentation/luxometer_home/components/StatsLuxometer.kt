package com.example.luxometromovil.presentation.luxometer_home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.luxometromovil.R
import com.example.luxometromovil.domain.model.UnitMeasuringLight

@Composable
fun StatsLuxometer(
    minMeasuringLight: Float,
    maxMeasuringLight: Float,
    unitMeasuringLight: UnitMeasuringLight,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Detalles Captados",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.padding(vertical = 6.dp))

            InfoStatsLuxometer(
                measuringLight = when (unitMeasuringLight) {
                    UnitMeasuringLight.LUX -> {
                        maxMeasuringLight
                    }

                    UnitMeasuringLight.FC -> {
                        maxMeasuringLight / 10.36f
                    }
                },
                title = "Iluminacion Maxima:"
            )

            Spacer(Modifier.padding(vertical = 6.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            Spacer(Modifier.padding(vertical = 6.dp))

            InfoStatsLuxometer(
                measuringLight = when (unitMeasuringLight) {
                    UnitMeasuringLight.LUX -> {
                        minMeasuringLight
                    }

                    UnitMeasuringLight.FC -> {
                        minMeasuringLight / 10.36f
                    }
                },
                title = "Iluminacion Minima:"
            )
        }

    }
}

@Composable
fun InfoStatsLuxometer(
    measuringLight: Float,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (measuringLight == Float.MIN_VALUE
                || measuringLight == Float.MAX_VALUE
                || measuringLight == Float.MIN_VALUE / 10.36f
                || measuringLight == Float.MAX_VALUE / 10.36f
            ) "0.00" else "%.2f".format(measuringLight),
            fontWeight = FontWeight.SemiBold
        )
    }
}