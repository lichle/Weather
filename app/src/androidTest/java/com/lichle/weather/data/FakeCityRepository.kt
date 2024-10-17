package com.lichle.weather.data

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeCityRepository : CityRepository {

    private val _weatherList = mutableListOf<Weather>()
    private val _weatherFlow = MutableStateFlow<List<Weather>>(emptyList())

    override fun fetchCityStream(id: Int): Flow<Weather> {
        val weather = _weatherList.find { it.id == id }
        return weather?.let { flowOf(it) } ?: flowOf()
    }

    override fun getCityListStream(): Flow<List<Weather>> {
        return _weatherFlow
    }

    override suspend fun fetchWeatherByCityId(city: String): Weather? {
        return _weatherList.find { it.name.equals(city, ignoreCase = true) }
    }

    override suspend fun getCity(id: Int): Weather? {
        return _weatherList.find { it.id == id }
    }

    override suspend fun addCity(weather: Weather) {
        _weatherList.add(weather)
        _weatherFlow.value = _weatherList.toList()  // Emit the updated list
    }

    override suspend fun deleteCity(id: Int) {
        val weatherToRemove = _weatherList.find { it.id == id }
        if (weatherToRemove != null) {
            _weatherList.remove(weatherToRemove)
            _weatherFlow.value = _weatherList.toList()  // Emit the updated list
        }
    }

}