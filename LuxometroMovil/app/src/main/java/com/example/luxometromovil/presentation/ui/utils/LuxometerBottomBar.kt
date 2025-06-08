package com.example.luxometromovil.presentation.ui.utils

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.luxometromovil.domain.model.IconsNavDestination

@Composable
fun LuxometerBottomAppBar(
    navController: NavHostController
) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val listItems = listOf(
        IconsNavDestination.Home,
        IconsNavDestination.Scenes,
        IconsNavDestination.Historial,
        IconsNavDestination.Configuration
    )
    NavigationBar {
        listItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index ,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) item.selectedItem else item.unselectedItem,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
            )
        }
    }
}