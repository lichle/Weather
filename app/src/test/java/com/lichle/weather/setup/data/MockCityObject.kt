package com.lichle.weather.setup.data

import com.lichle.weather.data.local.city.CityObject
import com.lichle.weather.data.local.city.WeatherSummaryObject

fun getMockCityObject(cityName: String): CityObject {
    return CityObject().apply {
        id = 1
        name = cityName
        lon = -74.0060
        lat = 40.7128
        temperature = 15.5
        feelsLike = 14.9
        tempMin = 10.0
        tempMax = 18.0
        pressure = 1012
        humidity = 60
        seaLevel = 1015
        groundLevel = 1005
        visibility = 10000
        windSpeed = 5.5
        windDeg = 180
        windGust = 7.8
        cloudiness = 75
        country = "US"
        sunrise = 1625214000L
        sunset = 1625261200L
        timestamp = 1625246400L
        timezone = -14400
        weather.add(getMockWeatherSummaryObject())  // Add the mock WeatherSummaryObject
    }
}

fun getMockWeatherSummaryObject(): WeatherSummaryObject {
    return WeatherSummaryObject().apply {
        id = 500
        main = "Rain"
        description = "light rain"
        icon = "10d"
    }
}