package com.example.madhumarganewmehafuzzzz.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<WeatherDescription>,
    val rain: Rain? = null
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class WeatherDescription(
    val description: String
)

data class Rain(
    val `1h`: Double? = 0.0
)
