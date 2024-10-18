package com.lichle.weather.setup.data

import com.lichle.weather.domain.City
import com.lichle.weather.domain.WeatherSummary

fun getMockCity(id: Int, name: String): City {
    return City(
        id = id,
        name = name,
        lon = 107.6,
        lat = 16.4667,
        temperature = 26.06,
        feelsLike = 26.06,
        tempMin = 24.0,
        tempMax = 28.0,
        pressure = 1012,
        humidity = 89,
        seaLevel = 1012,
        groundLevel = 1005,
        visibility = 10000,
        windSpeed = 1.54,
        windDeg = 190,
        windGust = 2.1,
        cloudiness = 40,
        weather = listOf(
            WeatherSummary(
                id = 500,
                main = "Rain",
                description = "light rain",
                icon = "10d"
            )
        ),
        country = "VN",
        sunrise = 1625214000,
        sunset = 1625261200,
        timestamp = 1625246400,
        timezone = 25200
    )
}