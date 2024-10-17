package com.lichle.weather.data.repository

import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    fun fetchCityStream(id: Int): Flow<City>
    fun getCityListStream(): Flow<List<City>>
    suspend fun fetchWeatherByCityId(city: String): City?
    suspend fun getCity(id: Int): City?
    suspend fun addCity(city: City)
    suspend fun deleteCity(id: Int)

}