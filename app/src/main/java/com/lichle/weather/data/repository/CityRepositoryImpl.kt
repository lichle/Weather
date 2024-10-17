package com.lichle.weather.data.repository

import com.lichle.weather.common.ApplicationScope
import com.lichle.weather.common.IoDispatcher
import com.lichle.weather.data.local.city.LocalCityDataSource
import com.lichle.weather.data.local.city.toDomain
import com.lichle.weather.data.local.city.toObject
import com.lichle.weather.data.remote.city.RemoteCityDataSource
import com.lichle.weather.data.remote.city.WeatherDto
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CityRepositoryImpl @Inject constructor(
    private val _local: LocalCityDataSource,
    private val _remote: RemoteCityDataSource,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher,
    @ApplicationScope private val _scope: CoroutineScope
) : CityRepository {

    override fun fetchCityStream(id: Int): Flow<Weather> {
        return object : NetworkBoundResource<Weather, WeatherDto>() {

            // Fetch weather from the remote API (RemoteWeatherDataSource)
            override suspend fun fetchFromNetwork(id: Int): WeatherDto? {
                return _remote.getWeather(id)
            }

            // Save the result of the remote API call into the local database (LocalWeatherDataSource)
            override suspend fun saveNetworkResult(item: WeatherDto) {
                _local.addCity(item.toObject())  // Assuming a mapping to entity
            }

            // Load weather data from the local database (Room)
            override fun loadFromDb(id: Int): Flow<Weather> {
                return _local.getCityFlow(id)
                    .map { it.toDomain() }  // Assuming a mapping to domain model
            }

        }.asFlow(id).flowOn(_dispatcher)
    }

    override fun getCityListStream(): Flow<List<Weather>> {
        return _local.getCityListFlow()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }.flowOn(_dispatcher)
    }

    override suspend fun fetchWeatherByCityId(city: String): Weather? = withContext(_dispatcher) {
        _remote.getWeatherByCity(city)?.toDomain()
    }

    override suspend fun getCity(id: Int): Weather? = withContext(_dispatcher) {
        _local.getCity(id)?.toDomain()
    }

    override suspend fun addCity(weather: Weather) = withContext(_dispatcher) {
        _local.addCity(weather.toObject())
    }

    override suspend fun deleteCity(id: Int) = withContext(_dispatcher) {
        _local.deleteCity(id)
    }

}