package com.lichle.weather.domain.weather

import com.lichle.weather.common.Logger
import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<NoRequest, List<Weather>>() {

    override suspend fun execute(request: NoRequest): Flow<List<Weather>> {
        // Call the repository method that returns a Flow of Weather list
        Logger.d(TAG, "GetWeatherListUseCase execute")
        return weatherRepository.getWeatherListStream()
    }

    companion object {
        private const val TAG = "GetWeatherListUseCase"
    }
}