package com.lichle.weather.data

import com.lichle.weather.data.local.weather.WeatherDao
import com.lichle.weather.data.local.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

class FakeLocalDataSource : WeatherDao {

    private var _weathers: MutableMap<Int, WeatherEntity>? = null

    var weathers: List<WeatherEntity>?
        get() = _weathers?.values?.toList()
        set(newList) {
            _weathers = newList?.associateBy { it.id }?.toMutableMap()
        }

    override fun getWeatherFlow(id: Int): Flow<WeatherEntity> {
        TODO("Not yet implemented")
    }

    override fun getWeatherListFlow(): Flow<List<WeatherEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeather(weather: WeatherEntity): Long {
        TODO("Not yet implemented")
    }

    override fun getWeather(id: Int): WeatherEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeather(id: Int): Int {
        TODO("Not yet implemented")
    }

}