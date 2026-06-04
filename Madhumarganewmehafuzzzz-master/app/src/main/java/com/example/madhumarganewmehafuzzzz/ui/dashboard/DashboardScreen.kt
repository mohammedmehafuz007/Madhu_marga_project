package com.example.madhumarganewmehafuzzzz.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madhumarganewmehafuzzzz.ui.weather.WeatherCard
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.example.madhumarganewmehafuzzzz.ui.theme.HoneyGold
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onManageHivesClick: () -> Unit = {},
    onViewMapsClick: () -> Unit = {},
    onViewAnalyticsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onAdminClick: () -> Unit = {}
) {
    val hives by viewModel.hives.collectAsState()
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Madhu-Marga Dashboard") },
                actions = {
                    IconButton(onClick = onAdminClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Admin")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onManageHivesClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Manage Hives")
                    }
                    Button(
                        onClick = {}, // TODO: Navigate to Harvest Tracker
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Log Harvest")
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onViewMapsClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Maps")
                    }
                    Button(
                        onClick = onViewAnalyticsClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Analytics")
                    }
                }
            }
            item {
                Button(
                    onClick = onChatClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("AI Beekeeping Assistant")
                }
            }
            item { SummaryCards(hives.size) }

            // Intervention Alerts (from project requirements)
            val criticalHives = hives.filter { it.activityLevel.contains("Low", ignoreCase = true) || it.condition == "Critical" }
            if (criticalHives.isNotEmpty()) {
                item {
                    InterventionAlertCard(criticalHives)
                }
            }

            item { FloraCalendarCard() }
            
            item { HoneyFlowSeasonCard() }
            
            item { HiveHealthCard() }
            item { HoneyProductionCard() }
            item { AiRecommendationsCard() }
        }
    }
}

@Composable
fun SummaryCards(hiveCount: Int = 0) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        WeatherCard()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("Total Hives", "$hiveCount", Modifier.weight(1f))
            StatCard("Healthy", "${(hiveCount * 0.8).toInt()}", Modifier.weight(1f))
        }
    }
}

@Composable
fun HoneyFlowSeasonCard() {
    // Simulated Honey Flow Season (Spring/Summer are usually peak)
    val month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
    val progress = when(month) {
        in 2..5 -> 0.9f // Peak
        in 6..8 -> 0.4f // Average
        else -> 0.1f // Dormant
    }
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Honey Flow Season", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress }, 
                modifier = Modifier.fillMaxWidth(),
                color = HoneyGold
            )
            Text(
                text = if (progress > 0.5f) "Peak Season: High Production" else "Off-Season: Maintenance Mode",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun HiveHealthCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hive Health Score", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = { 0.85f }, modifier = Modifier.fillMaxWidth())
            Text(text = "Overall Health: 85%", modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun HoneyProductionCard() {
    // Simplified model for demonstration
    val model = remember { 
        CartesianChartModel(
            LineCartesianLayerModel.build {
                series(5, 15, 10, 20, 25)
            }
        )
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Honey Production (kg)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                ),
                model = model,
                modifier = Modifier.height(200.dp)
            )
        }
    }
}

@Composable
fun AiRecommendationsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "AI Recommendations", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "• Hive 3 needs inspection due to unusual activity.")
            Text(text = "• Weather condition is optimal for honey harvesting.")
        }
    }
}

@Composable
fun InterventionAlertCard(criticalHives: List<com.example.madhumarganewmehafuzzzz.domain.model.Hive>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(8.dp))
                Text(text = "Intervention Alert!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            criticalHives.forEach { hive ->
                Text(text = "• Hive ${hive.hiveId}: ${hive.activityLevel} detected. Check for mites or temperature stress.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun FloraCalendarCard() {
    val currentMonth = java.util.Calendar.getInstance().getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, java.util.Locale.getDefault())
    val bloomingNow = com.example.madhumarganewmehafuzzzz.domain.model.IndianFloraCalendar.filter { it.bloomingMonths.contains(currentMonth) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Flora Calendar ($currentMonth)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (bloomingNow.isEmpty()) {
                Text("No major blooms detected this month.")
            } else {
                bloomingNow.forEach { flora ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = flora.flowerName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Nectar: ${flora.nectarLevel}", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(text = "Honey: ${flora.honeyType}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}
