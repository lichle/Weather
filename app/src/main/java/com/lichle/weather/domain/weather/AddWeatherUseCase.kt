package com.lichle.weather.domain.weather

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddWeatherUseCase @Inject constructor(
    private val _weatherRepository: WeatherRepository
): BaseUseCase<AddWeatherUseCase.AddRequest, Unit>() {

    override suspend fun execute(request: AddRequest): Flow<Unit> = flow {
        emit(_weatherRepository.addWeather(request.weather))
    }

    class AddRequest(val weather: Weather): Request<Weather>(data = (weather))

    companion object {
        private val TAG = AddWeatherUseCase::class.simpleName
    }

}