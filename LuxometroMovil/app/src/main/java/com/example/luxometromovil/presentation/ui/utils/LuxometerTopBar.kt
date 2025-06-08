package com.example.luxometromovil.presentation.ui.utils

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.luxometromovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxometerCenterTopBar(
    titleScreen: Int,
    canNavigateBack: Boolean,
) {
    CenterAlignedTopAppBar(
        title = { 
            Text(
                text = stringResource(id = titleScreen),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Sharp.ArrowBack,
                        contentDescription = stringResource(id = R.string.icon_arrow_back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}