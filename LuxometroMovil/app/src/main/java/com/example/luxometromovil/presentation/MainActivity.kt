package com.example.luxometromovil.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.luxometromovil.data.sensor.LightSensor
import com.example.luxometromovil.domain.model.NavDestination
import com.example.luxometromovil.presentation.camera_preview.CameraScreen
import com.example.luxometromovil.presentation.luxometer_configuration.ConfigurationScreen
import com.example.luxometromovil.presentation.luxometer_detail.DetailsItemScreen
import com.example.luxometromovil.presentation.luxometer_detail_history.DetailHistoryScreen
import com.example.luxometromovil.presentation.luxometer_home.HomeScreen
import com.example.luxometromovil.presentation.luxometer_list_history.ListHistoryScreen
import com.example.luxometromovil.presentation.luxometer_list_scene.ListSceneScreen
import com.example.luxometromovil.presentation.share_viewModel.SharedViewModel
import com.example.luxometromovil.presentation.ui.theme.LuxometroMovilTheme
import com.example.luxometromovil.presentation.ui.utils.LuxometerBottomAppBar
import dagger.hilt.android.AndroidEntryPoint

// git push -u origin main
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuxometroMovilTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val lightSensor = LightSensor(this)

                val sharedViewModel: SharedViewModel = hiltViewModel()
                val unitMeasuringLight by sharedViewModel.unitMeasuringLight.collectAsStateWithLifecycle()

                Scaffold(
                    bottomBar = { LuxometerBottomAppBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavDestination.HOME.route,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        composable(route = NavDestination.HOME.route) {
                            HomeScreen(
                                unitMeasuringLight = unitMeasuringLight,
                                onStartCamera = {
                                    navController.navigate(route = "${NavDestination.HOME.route}/camera")
                                }
                            )
                        }
                        composable(route = "${NavDestination.HOME.route}/camera") {
                            CameraScreen()
                        }
                        composable(route = NavDestination.SCENES.route) {
                            ListSceneScreen(navController = navController)
                        }
                        composable(route = "${NavDestination.SCENES.route}/{index}",
                            arguments = listOf(
                                navArgument(name = "index") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val index = backStackEntry.arguments?.getInt("index") ?: 0
                            DetailsItemScreen(index = index)
                        }
                        composable(route = NavDestination.HISTORIAL.route) {
                            ListHistoryScreen(unitMeasuringLight = unitMeasuringLight, navController = navController)
                        }
                        composable(route = "${NavDestination.HISTORIAL}/{index}",
                            arguments = listOf(
                                navArgument(name = "index") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val index = backStackEntry.arguments?.getInt("index") ?: 0
                            DetailHistoryScreen(
                                index = index
                            )
                        }
                        composable(route = NavDestination.CONFIGURATION.route) {
                            ConfigurationScreen(
                                informationSensor = lightSensor.getInformationSensor(),
                                updateMeasuringLight = { sharedViewModel.updateUnitMeasuringLight(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

