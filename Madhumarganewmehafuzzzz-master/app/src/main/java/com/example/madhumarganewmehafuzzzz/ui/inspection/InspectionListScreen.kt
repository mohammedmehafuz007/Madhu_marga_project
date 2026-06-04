package com.example.madhumarganewmehafuzzzz.ui.inspection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madhumarganewmehafuzzzz.domain.model.InspectionLog
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionListScreen(
    viewModel: InspectionViewModel,
    hiveId: String,
    onAddInspectionClick: () -> Unit,
    onBack: () -> Unit
) {
    val inspections by viewModel.inspections.collectAsState()

    LaunchedEffect(hiveId) {
        viewModel.loadInspections(hiveId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspections for $hiveId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddInspectionClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Inspection")
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
            items(inspections) { log ->
                InspectionItem(
                    log = log,
                    onDeleteClick = { viewModel.deleteInspectionLog(log.id) }
                )
            }
        }
    }
}

@Composable
fun InspectionItem(
    log: InspectionLog,
    onDeleteClick: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(log.inspectionDate))

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dateStr, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Text(text = "Observations: ${log.observations}")
            if (log.pestsFound.isNotEmpty()) {
                Text(text = "Pests: ${log.pestsFound.joinToString(", ")}")
            }
            if (log.diseasesFound.isNotEmpty()) {
                Text(text = "Diseases: ${log.diseasesFound.joinToString(", ")}")
            }
            if (log.notes.isNotEmpty()) {
                Text(text = "Notes: ${log.notes}")
            }
        }
    }
}
