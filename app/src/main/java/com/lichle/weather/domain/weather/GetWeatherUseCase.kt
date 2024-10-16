package com.lichle.weather.domain.weather

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
): BaseUseCase<GetWeatherUseCase.GetRequest, Weather>() {

    override suspend fun execute(request: GetRequest): Flow<Weather> = flow {
        val weather = weatherRepository.getWeather(request.id)
        if (weather != null) {
            emit(weather)
        } else {
            throw NoSuchElementException("Weather data not found for id: ${request.id}")
        }
    }

    class GetRequest(val id: Int): Request<Int>(data = id)

    companion object {
        private val TAG = AddWeatherUseCase::class.simpleName
    }

}