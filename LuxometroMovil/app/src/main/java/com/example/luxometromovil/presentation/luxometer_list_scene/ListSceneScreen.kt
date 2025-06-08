package com.example.luxometromovil.presentation.luxometer_list_scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.luxometromovil.domain.model.NavDestination
import com.example.luxometromovil.presentation.luxometer_list_scene.components.ItemLIstScene

@Composable
fun ListSceneScreen(
    listScenesViewModel: ListScenesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val listSceneUiState = listScenesViewModel.uiState.value
    LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        items(items = listSceneUiState.scenes) { scene ->
            ItemLIstScene(
                title = scene.title,
                painter = listScenesViewModel.getImgScene(scene.index),
                onClick = { navController.navigate(route = "${NavDestination.SCENES.route}/${scene.index}") }
            )
        }
    }
}