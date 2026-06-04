package com.example.madhumarganewmehafuzzzz.ui.hive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madhumarganewmehafuzzzz.domain.model.Hive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveListScreen(
    viewModel: HiveViewModel,
    onAddHiveClick: () -> Unit,
    onHiveClick: (Hive) -> Unit,
    onInspectClick: (Hive) -> Unit
) {
    val hives by viewModel.hives.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hives") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHiveClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Hive")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hives) { hive ->
                HiveItem(
                    hive = hive,
                    onClick = { onHiveClick(hive) },
                    onDeleteClick = { viewModel.deleteHive(hive.id) },
                    onInspectClick = { onInspectClick(hive) }
                )
            }
        }
    }
}

@Composable
fun HiveItem(
    hive: Hive,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onInspectClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "ID: ${hive.hiveId}", style = MaterialTheme.typography.titleMedium)
                Row {
                    TextButton(onClick = onInspectClick) {
                        Text("Inspect")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Text(text = "Location: ${hive.location}")
            Text(text = "Honey Level: ${hive.honeyLevel}%")
            Text(text = "Condition: ${hive.condition}")
        }
    }
}
