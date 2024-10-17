package com.lichle.weather.data.local.weather

import kotlinx.coroutines.flow.Flow

internal interface LocalWeatherDataSource {

    fun getWeatherFlow(id: Int): Flow<WeatherObject>
    fun getWeatherListFlow(): Flow<List<WeatherObject>>
    suspend fun getWeather(id: Int): WeatherObject?
    suspend fun addWeather(weather: WeatherObject)
    suspend fun deleteWeather(id: Int)

}