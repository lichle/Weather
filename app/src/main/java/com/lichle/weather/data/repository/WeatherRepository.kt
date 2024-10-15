package com.lichle.weather.data.repository

import com.lichle.weather.domain.Weather

interface WeatherRepository {

    suspend fun fetchWeatherByCity(city: String): Weather?
    suspend fun getWeather(id: Int): Weather?
    suspend fun addWeather(weather: Weather): Long
    suspend fun deleteWeather(id: Int): Int

}