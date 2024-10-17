package com.lichle.weather.view.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.core_common.ErrorCodes
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.weather.AddWeatherUseCase
import com.lichle.weather.domain.weather.FetchWeatherUseCase
import com.lichle.weather.domain.weather.GetWeatherUseCase
import com.lichle.weather.domain.weather.SearchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherState {
    data object Loading : WeatherState()
    data class FetchWeatherDataSuccess(
        val weather: WeatherUiModel,
        val isFavorite: Boolean
    ) : WeatherState()

    data class AddToFavoritesSuccess(val weather: WeatherUiModel) : WeatherState()
    data class Empty(val error: ErrorInfo? = null) : WeatherState()
}

data class ErrorInfo(val code: Int, val message: String)

sealed class WeatherIntent {
    data class SearchWeather(val cityName: String? = null, val cityId: Int = 0) : WeatherIntent()
    data object AddWeather : WeatherIntent()
    data object DismissError : WeatherIntent()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val _searchWeatherUseCase: SearchWeatherUseCase,
    private val _addWeatherUseCase: AddWeatherUseCase,
    private val _getWeatherUseCase: GetWeatherUseCase,
    private val _fetchWeatherUseCase: FetchWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Empty())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    private var _currentWeather: WeatherUiModel? = null

    fun processIntent(intent: WeatherIntent) {
        Logger.d(TAG, "Processing intent: $intent")
        when (intent) {
            is WeatherIntent.SearchWeather -> searchWeatherByCityOrId(
                intent.cityName, intent.cityId
            )

            is WeatherIntent.AddWeather -> addWeather()
            is WeatherIntent.DismissError -> dismissError()
        }
    }

    private fun searchWeatherByCityOrId(cityName: String?, cityId: Int) {
        Logger.d(TAG, "Search weather by cityName: $cityName, cityId: $cityId")
        viewModelScope.launch {
            _state.value = WeatherState.Loading
            when {
                !cityName.isNullOrBlank() -> searchWeatherByCity(cityName)
                cityId != 0 -> fetchWeather(cityId)
                else -> _state.value = WeatherState.Empty()
            }
        }
    }

    private suspend fun searchWeatherByCity(cityName: String) {
        Logger.d(TAG, "Search weather by city: $cityName")
        val request = SearchWeatherUseCase.SearchRequest(cityName)
        _searchWeatherUseCase(request).collect { response ->
            when (response) {
                is Response.Loading -> _state.value = WeatherState.Loading
                is Response.Success -> {
                    response.data?.let { weatherDto ->
                        val weather = weatherDto.toUiModel()
                        _currentWeather = weather
                        buildWeatherDataState(weather) {
                            _state.value =
                                WeatherState.FetchWeatherDataSuccess(it.weather, it.isFavorite)
                        }
                    } ?: WeatherState.Empty(
                        ErrorInfo(
                            ErrorCodes.NOT_FOUND, "Weather data not found"
                        )
                    )
                }

                is Response.Error -> {
                    Logger.d("WeatherViewModel", "Error occurred: ${response.message}")
                    _state.value = WeatherState.Empty(ErrorInfo(response.code, response.message))
                }
            }
        }
    }

    private suspend fun buildWeatherDataState(
        weather: WeatherUiModel,
        onResult: (WeatherState.FetchWeatherDataSuccess) -> Unit
    ) {
        val request = GetWeatherUseCase.GetRequest(weather.id)
        var weatherState = WeatherState.FetchWeatherDataSuccess(weather, false)
        _getWeatherUseCase(request).collect { response ->
            if (response is Response.Success) {
                weatherState = WeatherState.FetchWeatherDataSuccess(weather, true)
            }
        }
        onResult(weatherState)
    }

    private fun dismissError() {
        Logger.d(TAG, "Use dismissed on error")
    }

    private suspend fun fetchWeather(id: Int) {
        Logger.d(TAG, "Fetch weather by id: $id")
        val request = FetchWeatherUseCase.FetchRequest(id)
        _fetchWeatherUseCase(request).collect { response ->
            _state.value = when (response) {
                is Response.Loading -> WeatherState.Loading
                is Response.Success -> {
                    val weather = response.data.toUiModel()
                    _currentWeather = weather
                    WeatherState.FetchWeatherDataSuccess(weather, true)
                }

                is Response.Error -> {
                    WeatherState.Empty(ErrorInfo(response.code, response.message))
                }
            }
        }
    }

    private fun addWeather() {
        Logger.d(TAG, "Add weather to favorites")
        viewModelScope.launch {
            _currentWeather?.let { weather ->
                val currentWeather = weather.deepCopy()
                val request = AddWeatherUseCase.AddRequest(currentWeather.toDomain())
                _addWeatherUseCase(request).collect { response ->
                    when (response) {
                        is Response.Success -> {
//                            if (response.data > 0) {
//                                _currentWeather?.let {
//                                    _state.value = WeatherState.AddToFavoritesSuccess(it)
//                                }
//                            } else {
//                                _state.value = WeatherState.Empty(
//                                    ErrorInfo(ErrorCodes.DB_UNKNOWN_ERROR, "Weather data not saved")
//                                )
//                            }
                            _currentWeather?.let {
                                _state.value = WeatherState.AddToFavoritesSuccess(it)
                            }
                        }

                        is Response.Error -> {
                            _state.value =
                                WeatherState.Empty(ErrorInfo(response.code, response.message))
                        }

                        else -> {}
                    }
                }
            } ?: run {
                _state.value = WeatherState.Empty(
                    ErrorInfo(ErrorCodes.DB_UNKNOWN_ERROR, "No weather data to add")
                )
                return@launch
            }
        }
    }

    companion object {
        private const val TAG = "WeatherViewModel"
    }

}