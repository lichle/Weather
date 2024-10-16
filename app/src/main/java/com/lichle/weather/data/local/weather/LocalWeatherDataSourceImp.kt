package com.lichle.weather.data.local.weather

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LocalWeatherDataSourceImp @Inject constructor(private val weatherDao: WeatherDao) :
    LocalWeatherDataSource {

    override fun getWeatherFlow(id: Int): Flow<WeatherEntity> {
        return weatherDao.getWeatherFlow(id)
    }

    override fun getWeatherListFlow(): Flow<List<WeatherEntity>> {
        return weatherDao.getWeatherListFlow()
    }

    // Get a single weather record
    override suspend fun getWeather(id: Int): WeatherEntity? {
        return weatherDao.getWeather(id)
    }

    // Add or update a weather record in the database
    override suspend fun addWeather(weather: WeatherEntity): Long {
        return weatherDao.insertWeather(weather)
    }

    // Delete weather record
    override suspend fun deleteWeather(id: Int): Int {
        val weather = weatherDao.getWeather(id)
        return weather?.let {
            weatherDao.deleteWeather(it.id)
        } ?: 0
    }
}