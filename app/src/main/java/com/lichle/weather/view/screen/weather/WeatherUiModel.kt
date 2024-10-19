package com.lichle.weather.view.screen.weather

import android.os.Parcelable
import com.lichle.weather.domain.City
import com.lichle.weather.domain.WeatherSummary
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val seaLevel: Int,
    val groundLevel: Int,
    val visibility: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val cloudiness: Int,
    val weather: List<WeatherSummaryUiModel>, // Store the list of Weather objects
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long,
    val timezone: Int
) : Parcelable {
    fun deepCopy(): WeatherUiModel {
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
            weather = weather.map { it.deepCopy() }, // Deep copy each WeatherSummaryUiModel
            country = country,
            sunrise = sunrise,
            sunset = sunset,
            timestamp = timestamp,
            timezone = timezone
        )
    }
}

@Parcelize
data class WeatherSummaryUiModel(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable {
    fun deepCopy(): WeatherSummaryUiModel {
        return WeatherSummaryUiModel(
            id = id,
            main = main,
            description = description,
            icon = icon
        )
    }
}

internal fun City.toUiModel(): WeatherUiModel {
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


internal fun WeatherUiModel.toDomain(): City {
    return City(
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
        weather = this.weather.map { it.toDomain() }, // Store the entire weather list
        country = country,
        sunrise = sunrise,
        sunset = sunset,
        timestamp = timestamp,
        timezone = timezone
    )
}

internal fun WeatherSummaryUiModel.toDomain(): WeatherSummary {
    return WeatherSummary(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}