package com.lichle.weather.domain.city

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val _cityRepository: CityRepository
): BaseUseCase<SearchCityUseCase.SearchRequest, City?>() {

    override suspend fun execute(request: SearchRequest): Flow<City?> = flow {
        val weather = _cityRepository.fetchWeatherByCityId(request.city)
        emit(weather)
    }

    class SearchRequest(val city: String): Request<String>(data = (city))

    companion object {
        private val TAG = SearchCityUseCase::class.simpleName
    }

}