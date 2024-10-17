package com.lichle.weather.data

import com.lichle.weather.data.remote.weather.Clouds
import com.lichle.weather.data.remote.weather.Coord
import com.lichle.weather.data.remote.weather.Main
import com.lichle.weather.data.remote.weather.RemoteWeatherDataSource
import com.lichle.weather.data.remote.weather.Sys
import com.lichle.weather.data.remote.weather.WeatherDto
import com.lichle.weather.data.remote.weather.WeatherSummaryDto
import com.lichle.weather.data.remote.weather.Wind

class FakeNetworkDataSource : RemoteWeatherDataSource {

    override suspend fun getWeatherByCity(cityName: String): WeatherDto? {
        return fakeWeatherDto
    }

    override suspend fun getWeather(id: Int): WeatherDto? {
        return fakeWeatherDto
    }

    private val fakeWeatherDto = WeatherDto(
        coord = Coord(105.8412, 21.0245),
        weather = listOf(
            WeatherSummaryDto(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01n"
            )
        ),
        base = "stations",
        main = Main(
            temp = 27.0,
            feelsLike = 29.28,
            tempMin = 27.0,
            tempMax = 27.0,
            pressure = 1013,
            humidity = 75,
            seaLevel = 1013,
            groundLevel = 1011
        ),
        visibility = 10000,
        wind = Wind(
            speed = 3.28,
            deg = 165,
            gust = 6.94
        ),
        clouds = Clouds(
            all = 9
        ),
        dt = 1729083133,
        sys = Sys(
            type = 1,
            id = 9308,
            country = "VN",
            sunrise = 1729032736,
            sunset = 1729074706
        ),
        timezone = 25200,
        id = 1581130,
        name = "Hanoi",
        cod = 200
    )

}
