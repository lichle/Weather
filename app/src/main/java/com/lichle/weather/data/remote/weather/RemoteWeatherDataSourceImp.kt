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

    @Throws(NetworkException::class)
    override suspend fun getWeatherByCity(cityName: String): WeatherDto? {
        return apiService.getWeatherByCity(cityName)
    }

    companion object {
        private const val TAG = "RemoteWeatherDataSourceImpl"
    }

}