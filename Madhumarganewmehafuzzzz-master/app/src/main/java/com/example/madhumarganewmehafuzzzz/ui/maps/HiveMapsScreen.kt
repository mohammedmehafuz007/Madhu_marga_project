package com.example.madhumarganewmehafuzzzz.ui.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.madhumarganewmehafuzzzz.ui.hive.HiveViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveMapsScreen(
    viewModel: HiveViewModel,
    onBack: () -> Unit
) {
    val hives by viewModel.hives.collectAsState()
    val singapore = LatLng(1.35, 103.87) // Default fallback
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hive Locations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            hives.forEach { hive ->
                if (hive.latitude != 0.0 && hive.longitude != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(hive.latitude, hive.longitude)),
                        title = "Hive ${hive.hiveId}",
                        snippet = "Location: ${hive.location}"
                    )
                }
            }
        }
    }
}
