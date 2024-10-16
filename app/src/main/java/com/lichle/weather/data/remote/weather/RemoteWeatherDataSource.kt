package com.lichle.weather.data.remote.weather

interface RemoteWeatherDataSource {
    suspend fun getWeatherByCity(cityName: String): WeatherDto?
    suspend fun getWeather(id: Int): WeatherDto?
}