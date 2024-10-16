package com.lichle.weather.data.remote.weather

import android.content.res.Resources
import android.net.http.NetworkException
import com.lichle.weather.common.Logger
import com.lichle.weather.data.remote.ApiService
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

internal class RemoteWeatherDataSourceImp @Inject constructor(
    private val apiService: ApiService,
    @Named("apiKey") private val apiKey: String
) : RemoteWeatherDataSource {

    override suspend fun getWeatherByCity(cityName: String): WeatherDto? {
        return apiService.getWeatherByCity(cityName, apiKey)
    }

    override suspend fun getWeather(id: Int): WeatherDto? {
        return apiService.getWeather(id, apiKey)
    }

    companion object {
        private const val TAG = "RemoteWeatherDataSourceImpl"
    }

}