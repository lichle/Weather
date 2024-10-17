package com.lichle.weather.data.local.city

import kotlinx.coroutines.flow.Flow

internal interface LocalCityDataSource {

    fun getCityFlow(id: Int): Flow<CityObject>
    fun getCityListFlow(): Flow<List<CityObject>>
    suspend fun getCity(id: Int): CityObject?
    suspend fun addCity(city: CityObject)
    suspend fun deleteCity(id: Int)

}