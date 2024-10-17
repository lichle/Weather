package com.lichle.weather.data.repository

import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun fetchWeatherStream(id: Int): Flow<Weather>
    fun getWeatherListStream(): Flow<List<Weather>>
    suspend fun fetchWeatherByCity(city: String): Weather?
    suspend fun getWeather(id: Int): Weather?
    suspend fun addWeather(weather: Weather)
    suspend fun deleteWeather(id: Int)

}