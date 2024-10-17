package com.lichle.weather.data.remote.weather

import com.lichle.weather.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Named

internal class RemoteWeatherDataSourceImp @Inject constructor(
    private val _apiService: ApiService,
    @Named("apiKey") private val _apiKey: String
) : RemoteWeatherDataSource {

    override suspend fun getWeatherByCity(cityName: String): WeatherDto? {
        return _apiService.getWeatherByCity(cityName, _apiKey)
    }

    override suspend fun getWeather(id: Int): WeatherDto? {
        return _apiService.getWeather(id, _apiKey)
    }

    companion object {
        private const val TAG = "RemoteWeatherDataSourceImpl"
    }

}