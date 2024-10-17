package com.lichle.weather.view.screen.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.City
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.DeleteCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

sealed class CityListState {
    data object Loading : CityListState()
    data class Success(val cities: List<CityUiModel>) : CityListState()
    data class Error(val message: String) : CityListState()
}

sealed class CityListIntent {
    data object LoadFavorites : CityListIntent()
    data class DeleteCity(val cityId: Int) : CityListIntent()
    data class NavigateToWeatherDetail(val cityId: Int) : CityListIntent()
    data class SearchCity(val cityName: String) : CityListIntent()
}

@HiltViewModel
class CityListViewModel @Inject constructor(
    @Named("GetCityListUseCase") private val _getCityListUseCase: BaseUseCase<NoRequest, List<City>>,
    @Named("DeleteCityUseCase") private val _deleteCityUseCase: BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit>
) : ViewModel() {

    private val _state = MutableStateFlow<CityListState>(CityListState.Loading)
    val state: StateFlow<CityListState> = _state.asStateFlow()

    fun processIntent(intent: CityListIntent) {
        when (intent) {
            is CityListIntent.LoadFavorites -> fetchCityList()
            is CityListIntent.DeleteCity -> deleteCityById(intent.cityId)
            is CityListIntent.NavigateToWeatherDetail -> {} // Handle in UI
            is CityListIntent.SearchCity -> {} // Handle in UI
        }
    }

    private fun fetchCityList() {
        viewModelScope.launch {
            _state.value = CityListState.Loading
            _getCityListUseCase(NoRequest)
                .map { response ->
                    when (response) {
                        is Response.Loading -> CityListState.Loading
                        is Response.Success -> CityListState.Success(response.data.map { it.toCityUiModel() })
                        is Response.Error -> CityListState.Error(response.message)
                    }
                }
                .collect { state ->
                    _state.value = state
                }
        }
    }

    private fun deleteCityById(cityId: Int) {
        viewModelScope.launch {
            Logger.d(TAG, "deleteWeatherById, id: $cityId")
            _deleteCityUseCase(DeleteCityUseCase.DeleteRequest(cityId))
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Success -> {
                            Logger.d(TAG, "Weather with id: $cityId deleted successfully.")
                        }
                        is Response.Error -> {
                            Logger.e(TAG, "Error deleting weather: ${response.message}")
                            _state.value = CityListState.Error("Failed to delete city: ${response.message}")
                        }
                    }
                }
        }
    }

    companion object {
        private val TAG = CityListViewModel::class.java.simpleName
    }
}