package com.lichle.weather.domain.weather

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<FetchWeatherUseCase.FetchRequest, Weather>() {

    override suspend fun execute(request: FetchRequest): Flow<Weather> {
        return weatherRepository.fetchWeatherStream(request.id)
    }

    class FetchRequest(val id: Int) : Request<Int>(data = (id))

    companion object {
        private val TAG = AddWeatherUseCase::class.simpleName
    }

}