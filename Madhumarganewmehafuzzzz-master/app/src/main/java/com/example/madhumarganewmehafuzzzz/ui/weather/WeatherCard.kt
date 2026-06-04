package com.example.madhumarganewmehafuzzzz.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeatherCard(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weather by viewModel.weatherState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Default to some coordinates (e.g., a central location)
    // In a real app, you'd use the user's current location
    LaunchedEffect(Unit) {
        viewModel.fetchWeather(23.8103, 90.4125) // Example: Dhaka
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Local Weather Info",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                weather?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherStat("Temp", "${it.temperature}°C")
                        WeatherStat("Rainfall", "${it.rainfall}mm")
                        WeatherStat("Humidity", "${it.humidity}%")
                        WeatherStat("Wind", "${it.windSpeed}m/s")
                    }
                    Text(
                        text = "Condition: ${it.condition.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } ?: Text("Failed to load weather data")
            }
        }
    }
}

@Composable
fun WeatherStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}
