package com.lichle.weather.data.repository

import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    fun fetchCityStream(id: Int): Flow<Weather>
    fun getCityListStream(): Flow<List<Weather>>
    suspend fun fetchWeatherByCityId(city: String): Weather?
    suspend fun getCity(id: Int): Weather?
    suspend fun addCity(weather: Weather)
    suspend fun deleteCity(id: Int)

}