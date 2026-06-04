package com.example.madhumarganewmehafuzzzz.ui.hive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madhumarganewmehafuzzzz.domain.model.Hive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHiveScreen(
    viewModel: HiveViewModel,
    hiveId: String? = null,
    qrResult: String? = null,
    onScanQrClick: () -> Unit = {},
    onBack: () -> Unit
) {
    val existingHive = viewModel.hives.collectAsState().value.find { it.id == hiveId }

    var hiveIdText by remember { mutableStateOf(existingHive?.hiveId ?: "") }

    LaunchedEffect(qrResult) {
        if (qrResult != null) {
            hiveIdText = qrResult
        }
    }
    var locationText by remember { mutableStateOf(existingHive?.location ?: "") }
    var conditionText by remember { mutableStateOf(existingHive?.condition ?: "") }
    var queenStatusText by remember { mutableStateOf(existingHive?.queenStatus ?: "") }
    var honeyLevel by remember { mutableFloatStateOf(existingHive?.honeyLevel?.toFloat() ?: 0f) }
    var activityLevelText by remember { mutableStateOf(existingHive?.activityLevel ?: "") }
    var latitude by remember { mutableStateOf(existingHive?.latitude?.toString() ?: "") }
    var longitude by remember { mutableStateOf(existingHive?.longitude?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (hiveId == null) "Add Hive" else "Edit Hive") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = hiveIdText,
                onValueChange = { hiveIdText = it },
                label = { Text("Hive ID") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = onScanQrClick) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR")
                    }
                }
            )
            OutlinedTextField(
                value = locationText,
                onValueChange = { locationText = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = conditionText,
                onValueChange = { conditionText = it },
                label = { Text("Condition") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = queenStatusText,
                onValueChange = { queenStatusText = it },
                label = { Text("Queen Bee Status") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = "Honey Level: ${honeyLevel.toInt()}%")
            Slider(
                value = honeyLevel,
                onValueChange = { honeyLevel = it },
                valueRange = 0f..100f
            )
            OutlinedTextField(
                value = activityLevelText,
                onValueChange = { activityLevelText = it },
                label = { Text("Bee Activity Level") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    val newHive = Hive(
                        id = hiveId ?: "",
                        hiveId = hiveIdText,
                        location = locationText,
                        condition = conditionText,
                        queenStatus = queenStatusText,
                        honeyLevel = honeyLevel.toInt(),
                        activityLevel = activityLevelText,
                        latitude = latitude.toDoubleOrNull() ?: 0.0,
                        longitude = longitude.toDoubleOrNull() ?: 0.0
                    )
                    if (hiveId == null) {
                        viewModel.addHive(newHive)
                    } else {
                        viewModel.updateHive(newHive)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (hiveId == null) "Save Hive" else "Update Hive")
            }
        }
    }
}
