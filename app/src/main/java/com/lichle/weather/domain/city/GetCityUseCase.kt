package com.lichle.weather.domain.city

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityUseCase @Inject constructor(
    private val _cityRepository: CityRepository
): BaseUseCase<GetCityUseCase.GetRequest, City>() {

    override suspend fun execute(request: GetRequest): Flow<City> = flow {
        val weather = _cityRepository.getCity(request.id)
        if (weather != null) {
            emit(weather)
        } else {
            throw NoSuchElementException("Weather data not found for id: ${request.id}")
        }
    }

    class GetRequest(val id: Int): Request<Int>(data = id)

    companion object {
        private val TAG = AddCityUseCase::class.simpleName
    }

}