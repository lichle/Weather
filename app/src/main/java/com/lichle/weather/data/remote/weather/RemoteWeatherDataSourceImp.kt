package com.lichle.weather.data.remote.weather

import android.content.res.Resources
import android.net.http.NetworkException
import com.lichle.weather.common.Logger
import com.lichle.weather.data.remote.ApiService
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class RemoteWeatherDataSourceImp @Inject constructor(
    private val apiService: ApiService,
) : RemoteWeatherDataSource {

    override suspend fun getWeatherByCity(cityName: String): WeatherDto? {
        return apiService.getWeatherByCity(cityName)
    }

    override suspend fun getWeather(id: Int): WeatherDto? {
        return apiService.getWeather(id)
    }

    companion object {
        private const val TAG = "RemoteWeatherDataSourceImpl"
    }

}