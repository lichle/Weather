package com.lichle.weather.data.remote.city

interface RemoteCityDataSource {
    suspend fun getWeatherByCity(cityName: String): WeatherDto?
    suspend fun getWeather(id: Int): WeatherDto?
}