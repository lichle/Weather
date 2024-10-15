package com.lichle.weather.data.remote.weather

interface RemoteWeatherDataSource {
    suspend fun getWeatherByCity(cityName: String): WeatherDto?
}