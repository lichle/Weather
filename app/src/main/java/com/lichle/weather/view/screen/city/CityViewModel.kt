package com.lichle.weather.view.screen.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.DeleteCityUseCase
import com.lichle.weather.domain.city.GetCityListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FavoriteState {
    data object Loading : FavoriteState()
    data class Success(val cities: List<CityUiModel>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()
}

sealed class FavoriteIntent {
    data object LoadFavorites : FavoriteIntent()
    data class DeleteCity(val cityId: Int) : FavoriteIntent()
    data class NavigateToWeatherDetail(val cityId: Int) : FavoriteIntent()
    data class SearchCity(val cityName: String) : FavoriteIntent()
}

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val _getCityListUseCase: GetCityListUseCase,
    private val _deleteCityUseCase: DeleteCityUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    fun processIntent(intent: FavoriteIntent) {
        when (intent) {
            is FavoriteIntent.LoadFavorites -> fetchCityList()
            is FavoriteIntent.DeleteCity -> deleteCityById(intent.cityId)
            is FavoriteIntent.NavigateToWeatherDetail -> {} // Handle in UI
            is FavoriteIntent.SearchCity -> {} // Handle in UI
        }
    }

    private fun fetchCityList() {
        viewModelScope.launch {
            _state.value = FavoriteState.Loading
            _getCityListUseCase(NoRequest)
                .map { response ->
                    when (response) {
                        is Response.Loading -> FavoriteState.Loading
                        is Response.Success -> FavoriteState.Success(response.data.map { it.toCityUiModel() })
                        is Response.Error -> FavoriteState.Error(response.message)
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
                            _state.value = FavoriteState.Error("Failed to delete city: ${response.message}")
                        }
                    }
                }
        }
    }

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }
}