package com.lichle.weather.domain.city

import com.lichle.weather.common.Logger
import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCityListUseCase @Inject constructor(
    private val _cityRepository: CityRepository
) : BaseUseCase<NoRequest, List<City>>() {

    override suspend fun execute(request: NoRequest): Flow<List<City>> {
        // Call the repository method that returns a Flow of Weather list
        Logger.d(TAG, "GetWeatherListUseCase execute")
        return _cityRepository.getCityListStream()
    }

    companion object {
        private const val TAG = "GetWeatherListUseCase"
    }
}