package com.lichle.weather.data.local.weather

import com.lichle.weather.data.remote.weather.WeatherDto
import com.lichle.weather.data.remote.weather.WeatherSummaryDto
import com.lichle.weather.domain.Weather
import com.lichle.weather.domain.WeatherSummary
import io.realm.kotlin.ext.realmListOf

//------------------------- WeatherDto to WeatherObject -------------------------
internal fun WeatherDto.toObject(): WeatherObject {
    val weatherSummaryList = realmListOf<WeatherSummaryObject>()
    weather.forEach { weatherDto ->
        weatherSummaryList.add(weatherDto.toObject())
    }

    return WeatherObject().apply {
        this.id = this@toObject.id
        this.name = this@toObject.name
        this.lon = coord.lon
        this.lat = coord.lat
        this.temperature = main.temp
        this.feelsLike = main.feelsLike
        this.tempMin = main.tempMin
        this.tempMax = main.tempMax
        this.pressure = main.pressure
        this.humidity = main.humidity
        this.seaLevel = main.seaLevel
        this.groundLevel = main.groundLevel
        this.visibility = this@toObject.visibility
        this.windSpeed = wind.speed
        this.windDeg = wind.deg
        this.windGust = wind.gust
        this.cloudiness = clouds.all
        this.weather = weatherSummaryList
        this.country = sys.country
        this.sunrise = sys.sunrise
        this.sunset = sys.sunset
        this.timestamp = dt
        this.timezone = this@toObject.timezone
    }
}

internal fun WeatherSummaryDto.toObject(): WeatherSummaryObject {
    return WeatherSummaryObject().apply {
        id = this@toObject.id
        main = this@toObject.main
        description = this@toObject.description
        icon = this@toObject.icon
    }
}

//------------------------- Weather to WeatherObject -------------------------
internal fun Weather.toObject(): WeatherObject {
    val weatherSummaryList = realmListOf<WeatherSummaryObject>()
    this.weather.map { it.toObject() }
    return WeatherObject().apply {
        this.id = this@toObject.id
        this.name = this@toObject.name
        this.lon = this@toObject.lon
        this.lat = this@toObject.lat
        this.temperature = this@toObject.temperature
        this.feelsLike = this@toObject.feelsLike
        this.tempMin = this@toObject.tempMin
        this.tempMax = this@toObject.tempMax
        this.pressure = this@toObject.pressure
        this.humidity = this@toObject.humidity
        this.seaLevel = this@toObject.seaLevel
        this.groundLevel = this@toObject.groundLevel
        this.visibility = this@toObject.visibility
        this.windSpeed = this@toObject.windSpeed
        this.windDeg = this@toObject.windDeg
        this.windGust = this@toObject.windGust
        this.cloudiness = this@toObject.cloudiness
        this.weather = weatherSummaryList
        this.country = this@toObject.country
        this.sunrise = this@toObject.sunrise
        this.sunset = this@toObject.sunset
        this.timestamp = this@toObject.timestamp
        this.timezone = this@toObject.timezone
    }
}

internal fun WeatherSummary.toObject(): WeatherSummaryObject {
    return WeatherSummaryObject().apply {
        id = this@toObject.id
        main = this@toObject.main
        description = this@toObject.description
        icon = this@toObject.icon
    }
}

//------------------------- WeatherObject to Weather -------------------------
internal fun WeatherObject.toDomain(): Weather {
    return Weather(
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
        weather = weather.map { it.toDomain() }, // Store the entire weather list
        country = country,
        sunrise = sunrise,
        sunset = sunset,
        timestamp = timestamp,
        timezone = timezone
    )
}

internal fun WeatherSummaryObject.toDomain(): WeatherSummary {
    return WeatherSummary(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}


//------------------------- WeatherDto to Weather -------------------------
fun WeatherDto.toDomain(): Weather {
    return Weather(
        id = id,
        name = name,
        lon = coord.lon,
        lat = coord.lat,
        temperature = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        pressure = main.pressure,
        humidity = main.humidity,
        seaLevel = main.seaLevel,
        groundLevel = main.groundLevel,
        visibility = visibility,
        windSpeed = wind.speed,
        windDeg = wind.deg,
        windGust = wind.gust,
        cloudiness = clouds.all,
        weather = this.weather.map { it.toDomain() }, // Store the entire weather list
        country = sys.country,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        timestamp = dt,
        timezone = timezone
    )
}

fun WeatherSummaryDto.toDomain(): WeatherSummary {
    return WeatherSummary(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}
