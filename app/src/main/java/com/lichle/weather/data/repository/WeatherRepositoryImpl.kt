package com.lichle.weather.data.repository

import com.lichle.weather.common.ApplicationScope
import com.lichle.weather.common.IoDispatcher
import com.lichle.weather.data.local.NetworkBoundResource
import com.lichle.weather.data.local.weather.LocalWeatherDataSource
import com.lichle.weather.data.local.weather.toDomain
import com.lichle.weather.data.local.weather.toObject
import com.lichle.weather.data.remote.weather.RemoteWeatherDataSource
import com.lichle.weather.data.remote.weather.WeatherDto
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class WeatherRepositoryImpl @Inject constructor(
    private val _local: LocalWeatherDataSource,
    private val _remote: RemoteWeatherDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher,
    @ApplicationScope private val _scope: CoroutineScope
) : WeatherRepository {

    override fun fetchWeatherStream(id: Int): Flow<Weather> {
        return object : NetworkBoundResource<Weather, WeatherDto>() {

            // Fetch weather from the remote API (RemoteWeatherDataSource)
            override suspend fun fetchFromNetwork(id: Int): WeatherDto? {
                return _remote.getWeather(id)
            }

            // Save the result of the remote API call into the local database (LocalWeatherDataSource)
            override suspend fun saveNetworkResult(item: WeatherDto) {
                _local.addWeather(item.toObject())  // Assuming a mapping to entity
            }

            // Load weather data from the local database (Room)
            override fun loadFromDb(id: Int): Flow<Weather> {
                return _local.getWeatherFlow(id)
                    .map { it.toDomain() }  // Assuming a mapping to domain model
            }

        }.asFlow(id).flowOn(_dispatcher)
    }

    override fun getWeatherListStream(): Flow<List<Weather>> {
        return _local.getWeatherListFlow()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }.flowOn(_dispatcher)
    }

    override suspend fun fetchWeatherByCity(city: String): Weather? = withContext(_dispatcher) {
        _remote.getWeatherByCity(city)?.toDomain()
    }

    override suspend fun getWeather(id: Int): Weather? = withContext(_dispatcher) {
        _local.getWeather(id)?.toDomain()
    }

    override suspend fun addWeather(weather: Weather) = withContext(_dispatcher) {
        _local.addWeather(weather.toObject())
    }

    override suspend fun deleteWeather(id: Int) = withContext(_dispatcher) {
        _local.deleteWeather(id)
    }

}