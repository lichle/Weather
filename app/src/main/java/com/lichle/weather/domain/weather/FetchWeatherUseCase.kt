package com.lichle.weather.domain.weather

import com.lichle.weather.common.Logger
import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): BaseUseCase<String, Weather?>() {

    override suspend fun execute(request: String): Flow<Weather?> = flow {
        val weather = weatherRepository.fetchWeatherByCity(request)
        emit(weather)
    }

    companion object {
        private const val TAG = "FetchWeatherUseCase"
    }

}