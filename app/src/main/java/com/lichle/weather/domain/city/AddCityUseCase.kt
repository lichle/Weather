package com.lichle.weather.domain.city

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import com.lichle.weather.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val _cityRepository: CityRepository
): BaseUseCase<AddCityUseCase.AddRequest, Unit> {

    override suspend fun execute(request: AddRequest): Flow<Unit> = flow {
        emit(_cityRepository.addCity(request.weather))
    }

    class AddRequest(val weather: City): Request<City>(data = (weather))

    companion object {
        private val TAG = AddCityUseCase::class.simpleName
    }

}