package com.lichle.weather.data

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.Weather
import com.lichle.weather.domain.WeatherSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository : WeatherRepository {

    private val weatherList = mutableListOf<Weather>()
    private val weatherFlow = MutableStateFlow<List<Weather>>(emptyList())

    override fun fetchWeatherStream(id: Int): Flow<Weather> {
        val weather = weatherList.find { it.id == id }
        return weather?.let { flowOf(it) } ?: flowOf()
    }

    override fun getWeatherListStream(): Flow<List<Weather>> {
        return weatherFlow
    }

    override suspend fun fetchWeatherByCity(city: String): Weather? {
        return weatherList.find { it.name.equals(city, ignoreCase = true) }
    }

    override suspend fun getWeather(id: Int): Weather? {
        return weatherList.find { it.id == id }
    }

    override suspend fun addWeather(weather: Weather): Long {
        weatherList.add(weather)
        weatherFlow.value = weatherList.toList()  // Emit the updated list
        return weather.id.toLong()
    }

    override suspend fun deleteWeather(id: Int): Int {
        val weatherToRemove = weatherList.find { it.id == id }
        return if (weatherToRemove != null) {
            weatherList.remove(weatherToRemove)
            weatherFlow.value = weatherList.toList()  // Emit the updated list
            1  // Return success code
        } else {
            0  // Return failure code
        }
    }

}