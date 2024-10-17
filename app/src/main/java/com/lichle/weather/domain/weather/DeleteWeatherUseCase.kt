package com.lichle.weather.domain.weather

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteWeatherUseCase @Inject constructor(
    private val _weatherRepository: WeatherRepository
): BaseUseCase<DeleteWeatherUseCase.DeleteRequest, Unit>() {

    override suspend fun execute(request: DeleteRequest): Flow<Unit> = flow {
        emit(_weatherRepository.deleteWeather(request.id))
    }

    class DeleteRequest(val id: Int): Request<Int>(data = id)

    companion object {
        private val TAG = AddWeatherUseCase::class.simpleName
    }

}