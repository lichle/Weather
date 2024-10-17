package com.lichle.weather.domain.weather

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWeatherUseCase @Inject constructor(
    private val _weatherRepository: WeatherRepository
): BaseUseCase<SearchWeatherUseCase.SearchRequest, Weather?>() {

    override suspend fun execute(request: SearchRequest): Flow<Weather?> = flow {
        val weather = _weatherRepository.fetchWeatherByCity(request.city)
        emit(weather)
    }

    class SearchRequest(val city: String): Request<String>(data = (city))

    companion object {
        private val TAG = SearchWeatherUseCase::class.simpleName
    }

}