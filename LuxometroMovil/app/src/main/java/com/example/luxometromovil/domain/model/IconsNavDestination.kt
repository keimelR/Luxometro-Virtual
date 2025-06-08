package com.example.luxometromovil.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconsNavDestination(
    val title: String,
    val route: String,
    val selectedItem: ImageVector,
    val unselectedItem: ImageVector
) {
    object Home: IconsNavDestination(
        title = "Inicio",
        route = NavDestination.HOME.route,
        selectedItem = Icons.Filled.Home,
        unselectedItem = Icons.Outlined.Home
    )
    object Scenes: IconsNavDestination(
        title = "Escenas",
        route = NavDestination.SCENES.route,
        selectedItem = Icons.Filled.Place,
        unselectedItem = Icons.Outlined.Place
    )
    object Configuration: IconsNavDestination(
        title = "Ajustes",
        route = NavDestination.CONFIGURATION.route,
        selectedItem = Icons.Filled.Settings,
        unselectedItem = Icons.Outlined.Settings
    )
    object Historial: IconsNavDestination(
        title = "Historial",
        route = NavDestination.HISTORIAL.route,
        selectedItem = Icons.AutoMirrored.Filled.List,
        unselectedItem = Icons.AutoMirrored.Outlined.List
    )
}