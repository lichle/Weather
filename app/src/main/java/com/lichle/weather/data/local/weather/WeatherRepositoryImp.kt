package com.lichle.weather.data.local.weather

import com.lichle.weather.common.ApplicationScope
import com.lichle.weather.common.IoDispatcher
import com.lichle.weather.common.Logger
import com.lichle.weather.data.remote.weather.RemoteWeatherDataSource
import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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


//    override fun getWeatherStream(id: Int): Flow<Weather> {
//        return object : NetworkBoundResource<Weather, Weather>() {
//
//            // Load weather data from the local database (Room) based on ID
//            override suspend fun loadFromDb(): Flow<Weather> {
//                return local.getWeather(id) // Assuming this returns a Flow<WeatherEntity?>
//                    .map { entity ->
//                        // Map the entity to the domain model
//                        entity?.toDomain() ?: throw NoSuchElementException("No weather found for ID: $id")
//                    }
//            }
//
//            // Fetch weather data from the remote API based on ID
//            override suspend fun fetchFromNetwork(): Weather {
//                return remote.getWeatherByCity(id) // Assuming this returns a WeatherDto?
//                    .toDomain() // Convert the DTO to the domain model
//            }
//
//            // Save the result of the remote API call into the local database
//            override suspend fun saveNetworkResult(item: Weather) {
//                // Convert domain model to entity and save it into Room
//                local.addWeather(item.toEntity())
//            }
//
//        }.asFlow().flowOn(dispatcher) // Ensure the whole process runs on a background thread (e.g., Dispatchers.IO)
//    }

//    override fun getWeatherStream(): Flow<List<Weather>> {
//        return object : NetworkBoundResource<List<Weather>, List<Weather>>() {
//
//            // Load devices from the local database (Room)
//            override fun loadFromDb(): Flow<List<Weather>> {
//                return local.getWeathersStream()
//                    .map { entities ->
//                        entities.map { it.toDomain() }
//                    }
//            }
//
//            // Fetch devices from the remote API (remote data source)
//            override suspend fun fetchFromNetwork(): List<Weather> {
//                return remote.getDevicesStream().first()
//            }
//
//            // Save the result of the remote API call into the local database
//            override suspend fun saveNetworkResult(item: List<Weather>) {
//                local.addWeather(item)  // Save directly to Room
//
//            }
//
//        }.asFlow().flowOn(dispatcher)
//    }

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