package com.lichle.weather.data.remote

import com.lichle.weather.common.TempUnit
import com.lichle.weather.data.remote.weather.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String = TempUnit.CELSIUS.value
    ): WeatherDto?

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("id") id: Int,
        @Query("APPID") apiKey: String,
        @Query("units") units: String = TempUnit.CELSIUS.value
    ): WeatherDto?

}