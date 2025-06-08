package com.example.luxometromovil.presentation.luxometer_list_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.luxometromovil.domain.model.NavDestination
import com.example.luxometromovil.domain.model.UnitMeasuringLight

@Composable
fun ListHistoryScreen(
    listHistoryViewModel: ListHistoryViewModel = hiltViewModel(),
    navController: NavController,
    unitMeasuringLight: UnitMeasuringLight
) {
    val listHistoryUiState by listHistoryViewModel.uiState.collectAsStateWithLifecycle()

    if (listHistoryUiState.itemsHistoryUi.isEmpty()) {
        listHistoryViewModel.onEvent(ListHistoryEvent.GetAllItemHistory)
    }
    ListHistoryContent(
        listHistoryUiState = listHistoryUiState,
        navController = navController,
        addItemHistoryEvent = {
            listHistoryViewModel.onEvent(
                ListHistoryEvent.AddNewItemHistory(
                    title = "Prueba 1",
                    unitMeasuringLight = unitMeasuringLight
                )
            )
        },
        deleteItem = { listHistoryViewModel.onEvent(ListHistoryEvent.DeleteItemHistory(it)) },
        updateTittleHistoryEvent = { id, newTitle ->
            listHistoryViewModel.onEvent(
                ListHistoryEvent.UpdateTitleItemHistory(
                    id = id,
                    newTitle = newTitle
                )
            )
        }
    )
}

@Composable
fun ListHistoryContent(
    listHistoryUiState: ListHistoryUiState,
    navController: NavController,
    addItemHistoryEvent: () -> Unit,
    updateTittleHistoryEvent: (Int, String) -> Unit,
    deleteItem: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(listHistoryUiState.itemsHistoryUi) { item ->
                ItemListHistory(
                    title = item.title,
                    minMeasuringLight = item.minMeasuringLight,
                    maxMeasuringLight = item.maxMeasuringLight,
                    avgMeasuringLight = item.avgMeasuringLight,
                    unitMeasuringLight = item.unitMeasuringLight,
                    onClick = { navController.navigate(route = "${NavDestination.HISTORIAL}/${item.id}") },
                    deleteItem = { deleteItem(item.id) },
                    updateTittleHistoryEvent = { newTitle -> updateTittleHistoryEvent(item.id, newTitle) }
                )
            }
        }
        AddItemFloatingActionButton(
            onClick = { addItemHistoryEvent() }
        )
    }
}

@Composable
fun ItemListHistory(
    title: String,
    minMeasuringLight: Double,
    maxMeasuringLight: Double,
    avgMeasuringLight: Double,
    unitMeasuringLight: String,
    onClick: () -> Unit,
    deleteItem: () -> Unit,
    updateTittleHistoryEvent: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            horizontalAlignment = Alignment.End
        ) {
            Row {
                IconEventHistory(
                    onEvent = { deleteItem() },
                    imageVector = Icons.Filled.Delete,
                    title = "Eliminar"
                )
                IconEventHistory(
                    onEvent = { showDialog = true },
                    imageVector = Icons.Filled.Build,
                    title = "Modificar"
                )
            }
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        TextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Nuevo Nombre") }
                        )

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("Cancelar")
                            }
                            TextButton(
                                onClick = {
                                    updateTittleHistoryEvent(newTitle)
                                    showDialog = false
                                }
                            ) {
                                Text("Confirmar")
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp),
            ) {
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    maxLines = 3,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(0.6f)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "Min: $minMeasuringLight $unitMeasuringLight")
                    Text(text = "Max: $maxMeasuringLight $unitMeasuringLight")
                    Text(text = "Prom: $avgMeasuringLight $unitMeasuringLight")
                }
            }
        }
    }
}

@Composable
fun IconEventHistory(
    onEvent: () -> Unit,
    imageVector: ImageVector,
    title: String
) {
    IconButton(
        onClick = onEvent
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun AddItemFloatingActionButton(onClick: () -> Unit) {
    Box(
        Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar"
            )
        }
    }
}