package com.example.madhumarganewmehafuzzzz.ui.inspection

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madhumarganewmehafuzzzz.domain.model.InspectionLog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInspectionScreen(
    viewModel: InspectionViewModel,
    hiveId: String,
    onBack: () -> Unit
) {
    var observations by remember { mutableStateOf("") }
    var pests by remember { mutableStateOf("") }
    var diseases by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                observations += " ${data?.get(0) ?: ""}"
            }
        }
    )

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Inspection") },
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
                value = observations,
                onValueChange = { observations = it },
                label = { Text("Observations") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your observation")
                        }
                        speechLauncher.launch(intent)
                    }) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Input")
                    }
                }
            )
            OutlinedTextField(
                value = pests,
                onValueChange = { pests = it },
                label = { Text("Pests Found (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = diseases,
                onValueChange = { diseases = it },
                label = { Text("Diseases Found (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            // Image Picker Section
            Text(text = "Inspection Image", style = MaterialTheme.typography.titleMedium)
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }
            OutlinedButton(
                onClick = { imageLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Image")
            }

            if (isUploading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    scope.launch {
                        isUploading = true
                        var imageUrl: String? = null
                        selectedImageUri?.let { uri ->
                            val result = viewModel.uploadImage(uri, hiveId)
                            if (result.isSuccess) {
                                imageUrl = result.getOrNull()
                            }
                        }
                        
                        val log = InspectionLog(
                            hiveId = hiveId,
                            observations = observations,
                            pestsFound = pests.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            diseasesFound = diseases.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            notes = notes,
                            imageUrl = imageUrl // Update model if necessary
                        )
                        viewModel.addInspectionLog(log)
                        isUploading = false
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = observations.isNotEmpty() && !isUploading
            ) {
                Text("Save Inspection")
            }
        }
    }
}
