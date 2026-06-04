package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.data.network.WeatherApiService
import com.example.madhumarganewmehafuzzzz.domain.model.Weather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService
) {
    // Note: In a real app, the API key should be in a secure place or BuildConfig
    private val apiKey = "d94e22e967a6d96206f6b7a2d46e3089" // Placeholder key

    suspend fun getWeatherData(lat: Double, lon: Double): Weather {
        val response = apiService.getCurrentWeather(lat, lon, apiKey)
        return Weather(
            temperature = response.main.temp,
            rainfall = response.rain?.`1h` ?: 0.0,
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            condition = response.weather.firstOrNull()?.description ?: "Unknown"
        )
    }
}
