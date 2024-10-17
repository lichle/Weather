package com.lichle.weather.data

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeCityRepository : CityRepository {

    private val _cityList = mutableListOf<City>()
    private val _cityFlow = MutableStateFlow<List<City>>(emptyList())

    override fun fetchCityStream(id: Int): Flow<City> {
        val weather = _cityList.find { it.id == id }
        return weather?.let { flowOf(it) } ?: flowOf()
    }

    override fun getCityListStream(): Flow<List<City>> {
        return _cityFlow
    }

    override suspend fun fetchWeatherByCityId(city: String): City? {
        return _cityList.find { it.name.equals(city, ignoreCase = true) }
    }

    override suspend fun getCity(id: Int): City? {
        return _cityList.find { it.id == id }
    }

    override suspend fun addCity(city: City) {
        _cityList.add(city)
        _cityFlow.value = _cityList.toList()  // Emit the updated list
    }

    override suspend fun deleteCity(id: Int) {
        val weatherToRemove = _cityList.find { it.id == id }
        if (weatherToRemove != null) {
            _cityList.remove(weatherToRemove)
            _cityFlow.value = _cityList.toList()  // Emit the updated list
        }
    }

}