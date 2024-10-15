package com.lichle.weather.domain

data class Weather(
    val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int?,
    val groundLevel: Int?,
    val visibility: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double?,
    val cloudiness: Int,
    val weather: List<WeatherSummary>, // Store the list of Weather objects
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long,
    val timezone: Int
)

data class WeatherSummary(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)