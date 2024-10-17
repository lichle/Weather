package com.lichle.weather.data

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository : WeatherRepository {

    private val _weatherList = mutableListOf<Weather>()
    private val _weatherFlow = MutableStateFlow<List<Weather>>(emptyList())

    override fun fetchWeatherStream(id: Int): Flow<Weather> {
        val weather = _weatherList.find { it.id == id }
        return weather?.let { flowOf(it) } ?: flowOf()
    }

    override fun getWeatherListStream(): Flow<List<Weather>> {
        return _weatherFlow
    }

    override suspend fun fetchWeatherByCity(city: String): Weather? {
        return _weatherList.find { it.name.equals(city, ignoreCase = true) }
    }

    override suspend fun getWeather(id: Int): Weather? {
        return _weatherList.find { it.id == id }
    }

    override suspend fun addWeather(weather: Weather) {
        _weatherList.add(weather)
        _weatherFlow.value = _weatherList.toList()  // Emit the updated list
    }

    override suspend fun deleteWeather(id: Int) {
        val weatherToRemove = _weatherList.find { it.id == id }
        if (weatherToRemove != null) {
            _weatherList.remove(weatherToRemove)
            _weatherFlow.value = _weatherList.toList()  // Emit the updated list
        }
    }

}