package com.lichle.weather.data.local.weather

import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalWeatherDataSourceImpl @Inject constructor(
    private val _realm: Realm): LocalWeatherDataSource {

    override fun getWeatherFlow(id: Int): Flow<WeatherObject> {
        return _realm.query(WeatherObject::class, "id == $0", id).asFlow()
            .map { it.list.first() }
    }

    override fun getWeatherListFlow(): Flow<List<WeatherObject>> {
        return _realm.query(WeatherObject::class).asFlow().map { it.list }
    }

    override suspend fun getWeather(id: Int): WeatherObject? {
        return _realm.query(WeatherObject::class, "id == $0", id).first().find()
    }

    override suspend fun addWeather(weather: WeatherObject) {
        _realm.write { copyToRealm(weather) }
    }

    override suspend fun deleteWeather(id: Int) {
        _realm.write {
            val weather = query(WeatherObject::class, "id == $0", id).first().find()
            weather?.let { delete(it) }
        }
    }

}