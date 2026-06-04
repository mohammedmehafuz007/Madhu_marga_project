package com.example.madhumarganewmehafuzzzz.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.madhumarganewmehafuzzzz.domain.model.DiseaseReport
import com.example.madhumarganewmehafuzzzz.domain.model.User
import java.text.SimpleDateFormat
import java.util.*

/**
 * AdminDashboardScreen provides a centralized interface for administrators to monitor
 * beekeeping activities across all farmers and manage critical health reports.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onFarmerClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Farmers", "Disease Reports")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> FarmersList(viewModel, onFarmerClick)
                1 -> DiseaseReportsList(viewModel)
            }
        }
    }
}

@Composable
fun FarmersList(viewModel: AdminViewModel, onFarmerClick: (String) -> Unit) {
    val farmers by viewModel.farmers.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(farmers) { farmer ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                onClick = { onFarmerClick(farmer.uid) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = farmer.displayName ?: "Unknown Farmer", style = MaterialTheme.typography.titleMedium)
                    Text(text = farmer.email ?: "No Email", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun DiseaseReportsList(viewModel: AdminViewModel) {
    val reports by viewModel.reports.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(reports) { report ->
            DiseaseReportCard(report) { newStatus ->
                viewModel.updateReportStatus(report.id, newStatus)
            }
        }
    }
}

@Composable
fun DiseaseReportCard(report: DiseaseReport, onStatusChange: (String) -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (report.severity.name == "CRITICAL") MaterialTheme.colorScheme.errorContainer 
                            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = report.diseaseName, style = MaterialTheme.typography.titleLarge)
                Icon(Icons.Default.Warning, contentDescription = null)
            }
            Text(text = "Reported on: ${dateFormat.format(Date(report.reportedAt))}")
            Text(text = "Severity: ${report.severity}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${report.status}", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = report.description)
            
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onStatusChange("INVESTIGATING") }) { Text("Investigate") }
                OutlinedButton(onClick = { onStatusChange("RESOLVED") }) { Text("Resolve") }
            }
        }
    }
}
