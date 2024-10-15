package com.lichle.weather.data.local.weather

import com.lichle.weather.data.remote.weather.WeatherDto
import com.lichle.weather.data.remote.weather.WeatherSummaryDto
import com.lichle.weather.domain.Weather
import com.lichle.weather.domain.WeatherSummary

//------------------------- WeatherDto to WeatherEntity -------------------------
internal fun WeatherDto.toEntity(): WeatherEntity {
    return WeatherEntity(
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
        weather = this.weather.map { it.toEntity() }, // Store the entire weather list
        country = sys.country,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        timestamp = dt,
        timezone = timezone
    )
}

internal fun WeatherSummaryDto.toEntity(): WeatherSummaryEntity {
    return WeatherSummaryEntity(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}

internal fun WeatherSummaryEntity.toDto(): WeatherSummaryDto {
    return WeatherSummaryDto(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}

//------------------------- Weather to WeatherEntity -------------------------
internal fun Weather.toEntity(): WeatherEntity {
    return WeatherEntity(
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
        weather = this.weather.map { it.toEntity() }, // Store the entire weather list
        country = country,
        sunrise = sunrise,
        sunset = sunset,
        timestamp = timestamp,
        timezone = timezone
    )
}

internal fun WeatherSummary.toEntity(): WeatherSummaryEntity {
    return WeatherSummaryEntity(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}

//------------------------- WeatherEntity to Weather -------------------------
internal fun WeatherEntity.toDomain(): Weather {
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
        weather = this.weather.map { it.toDomain() }, // Store the entire weather list
        country = country,
        sunrise = sunrise,
        sunset = sunset,
        timestamp = timestamp,
        timezone = timezone
    )
}

internal fun WeatherSummaryEntity.toDomain(): WeatherSummary {
    return WeatherSummary(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}

//------------------------- WeatherDto to Weather -------------------------
internal fun WeatherDto.toDomain(): Weather {
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

internal fun WeatherSummaryDto.toDomain(): WeatherSummary {
    return WeatherSummary(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}