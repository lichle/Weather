package com.lichle.weather.setup.data

import com.lichle.weather.data.remote.city.Clouds
import com.lichle.weather.data.remote.city.Coord
import com.lichle.weather.data.remote.city.Main
import com.lichle.weather.data.remote.city.Sys
import com.lichle.weather.data.remote.city.WeatherDto
import com.lichle.weather.data.remote.city.WeatherSummaryDto
import com.lichle.weather.data.remote.city.Wind

fun getMockWeatherDto(): WeatherDto {
    return WeatherDto(
        coord = Coord(lon = 107.6, lat = 16.4667),
        weather = listOf(
            WeatherSummaryDto(
                id = 500,
                main = "Rain",
                description = "light rain",
                icon = "10d"
            )
        ),
        base = "stations",
        main = Main(
            temp = 26.06,
            feelsLike = 26.06,
            tempMin = 26.06,
            tempMax = 26.06,
            pressure = 1012,
            humidity = 89,
            seaLevel = 1012,
            groundLevel = 1000
        ),
        visibility = 10000,
        wind = Wind(
            speed = 1.54,
            deg = 190,
            gust = 2.1
        ),
        clouds = Clouds(
            all = 40
        ),
        dt = 1625246400,
        sys = Sys(
            type = 1,
            id = 9310,
            country = "VN",
            sunrise = 1625214000,
            sunset = 1625261200
        ),
        timezone = 25200,
        id = 1580240,
        name = "Hue",
        cod = 200
    )
}