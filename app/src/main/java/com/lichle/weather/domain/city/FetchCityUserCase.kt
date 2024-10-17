package com.lichle.weather.domain.city

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCityUserCase @Inject constructor(
    private val _cityRepository: CityRepository
) : BaseUseCase<FetchCityUserCase.FetchRequest, Weather>() {

    override suspend fun execute(request: FetchRequest): Flow<Weather> {
        return _cityRepository.fetchCityStream(request.id)
    }

    class FetchRequest(val id: Int) : Request<Int>(data = (id))

    companion object {
        private val TAG = AddCityUseCase::class.simpleName
    }

}