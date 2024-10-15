package com.lichle.weather.data.local.weather

import kotlinx.coroutines.flow.Flow

internal interface LocalWeatherDataSource {

    fun getWeatherStream(id: Int): Flow<WeatherEntity>
    fun getWeathersStream(): Flow<List<WeatherEntity>>
    suspend fun getWeather(id: Int): WeatherEntity?
    suspend fun addWeather(weather: WeatherEntity): Long
    suspend fun deleteWeather(id: Int): Int

}