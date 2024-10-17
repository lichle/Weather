package com.lichle.weather.view.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.weather.DeleteWeatherUseCase
import com.lichle.weather.domain.weather.GetWeatherListUseCase
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
    private val _getWeatherListUseCase: GetWeatherListUseCase,
    private val _deleteWeatherUseCase: DeleteWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    fun processIntent(intent: FavoriteIntent) {
        when (intent) {
            is FavoriteIntent.LoadFavorites -> fetchFavoriteList()
            is FavoriteIntent.DeleteCity -> deleteWeatherById(intent.cityId)
            is FavoriteIntent.NavigateToWeatherDetail -> {} // Handle in UI
            is FavoriteIntent.SearchCity -> {} // Handle in UI
        }
    }

    private fun fetchFavoriteList() {
        viewModelScope.launch {
            _state.value = FavoriteState.Loading
            _getWeatherListUseCase(NoRequest)
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

    private fun deleteWeatherById(cityId: Int) {
        viewModelScope.launch {
            Logger.d(TAG, "deleteWeatherById, id: $cityId")
            _deleteWeatherUseCase(DeleteWeatherUseCase.DeleteRequest(cityId))
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {} // Optionally handle loading state
                        is Response.Success -> {
                            Logger.d(TAG, "Weather with id: $cityId deleted successfully.")
//                            fetchFavoriteList() // Refresh the list after deletion
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