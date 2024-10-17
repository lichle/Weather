package com.lichle.weather.domain.city

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.Request
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCityUseCase @Inject constructor(
    private val _cityRepository: CityRepository
): BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit> {

    override suspend fun execute(request: DeleteRequest): Flow<Unit> = flow {
        emit(_cityRepository.deleteCity(request.id))
    }

    class DeleteRequest(val id: Int): Request<Int>(data = id)

    companion object {
        private val TAG = AddCityUseCase::class.simpleName
    }

}