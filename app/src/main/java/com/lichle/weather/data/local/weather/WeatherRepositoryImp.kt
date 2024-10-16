package com.lichle.weather.data.local.weather

import com.lichle.weather.common.ApplicationScope
import com.lichle.weather.common.IoDispatcher
import com.lichle.weather.data.local.NetworkBoundResource
import com.lichle.weather.data.remote.weather.RemoteWeatherDataSource
import com.lichle.weather.data.remote.weather.WeatherDto
import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class WeatherRepositoryImp @Inject constructor(
    private val local: LocalWeatherDataSource,
    private val remote: RemoteWeatherDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : WeatherRepository {

    override suspend fun fetchWeatherByCity(city: String): Weather? = withContext(dispatcher) {
        remote.getWeatherByCity(city)?.toDomain()
    }

    override fun fetchWeatherStream(id: Int): Flow<Weather> {
        return object : NetworkBoundResource<Weather, WeatherDto>() {

            // Fetch weather from the remote API (RemoteWeatherDataSource)
            override suspend fun fetchFromNetwork(id: Int): WeatherDto? {
                return remote.getWeather(id)
            }

            // Save the result of the remote API call into the local database (LocalWeatherDataSource)
            override suspend fun saveNetworkResult(item: WeatherDto) {
                local.addWeather(item.toEntity())  // Assuming a mapping to entity
            }

            // Load weather data from the local database (Room)
            override fun loadFromDb(id: Int): Flow<Weather> {
                return local.getWeatherFlow(id).map { it.toDomain() }  // Assuming a mapping to domain model
            }

        }.asFlow(id).flowOn(dispatcher)
    }

    override fun getWeatherListStream(): Flow<List<Weather>> {
        return local.getWeatherListFlow()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }.flowOn(dispatcher)
    }

    override suspend fun getWeather(id: Int): Weather? = withContext(dispatcher) {
        local.getWeather(id)?.toDomain()
    }

    override suspend fun addWeather(weather: Weather): Long = withContext(dispatcher) {
        local.addWeather(weather.toEntity())
    }

    override suspend fun deleteWeather(id: Int): Int = withContext(dispatcher) {
        local.deleteWeather(id)
    }

}