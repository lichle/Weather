package com.lichle.weather.view.screen.weather

import com.lichle.weather.domain.Weather
import com.lichle.weather.domain.WeatherSummary

data class WeatherUiModel(
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
    val weather: List<WeatherSummaryUiModel>, // Store the list of Weather objects
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long,
    val timezone: Int
)

data class WeatherSummaryUiModel(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

internal fun Weather.toUiModel(): WeatherUiModel {
    return WeatherUiModel(
        id = id,
        name = name,
        lon = lon,
        lat = lat,
        temperature = temperature,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        cloudiness = cloudiness,
        weather = this.weather.map { it.toUiModel() }, // Store the entire weather list
        country = country,
        sunrise = sunrise,
        sunset = sunset,
        timestamp = timestamp,
        timezone = timezone
    )
}

internal fun WeatherSummary.toUiModel(): WeatherSummaryUiModel {
    return WeatherSummaryUiModel(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}