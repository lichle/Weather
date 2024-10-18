package com.lichle.weather.view.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.core_common.ErrorCodes
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.City
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.AddCityUseCase
import com.lichle.weather.domain.city.FetchCityUserCase
import com.lichle.weather.domain.city.GetCityUseCase
import com.lichle.weather.domain.city.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

internal sealed class WeatherState {
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
    @Named("SearchCityUseCase") private val _searchCityUseCase: BaseUseCase<SearchCityUseCase.SearchRequest, City?>,
    @Named("AddCityUseCase") private val _addCityUseCase: BaseUseCase<AddCityUseCase.AddRequest, Unit>,
    @Named("GetCityUseCase") private val _getCityUseCase: BaseUseCase<GetCityUseCase.GetRequest, City>,
    @Named("FetchCityUserCase") private val _fetchCityUseCase: BaseUseCase<FetchCityUserCase.FetchRequest, City>
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Empty())
    internal val state: StateFlow<WeatherState> = _state.asStateFlow()

    private var _currentWeather: WeatherUiModel? = null

    fun processIntent(intent: WeatherIntent) {
        Logger.d(TAG, "Processing intent: $intent")
        when (intent) {
            is WeatherIntent.SearchWeather -> searchWeatherByCityOrId(
                intent.cityName, intent.cityId
            )

            is WeatherIntent.AddWeather -> addCity()
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
        viewModelScope.launch {
            val request = SearchCityUseCase.SearchRequest(cityName)
            _searchCityUseCase(request).collect { response ->
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
    }

    private suspend fun buildWeatherDataState(
        weather: WeatherUiModel,
        onResult: (WeatherState.FetchWeatherDataSuccess) -> Unit
    ) {
        viewModelScope.launch {
            val request = GetCityUseCase.GetRequest(weather.id)
            var weatherState = WeatherState.FetchWeatherDataSuccess(weather, false)
            _getCityUseCase(request).collect { response ->
                if (response is Response.Success) {
                    weatherState = WeatherState.FetchWeatherDataSuccess(weather, true)
                }
            }
            onResult(weatherState)
        }
    }

    private fun dismissError() {
        Logger.d(TAG, "Use dismissed on error")
    }

    private suspend fun fetchWeather(id: Int) {
        Logger.d(TAG, "Fetch weather by id: $id")
        viewModelScope.launch {
            val request = FetchCityUserCase.FetchRequest(id)
            _fetchCityUseCase(request).collect { response ->
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
    }

    private fun addCity() {
        Logger.d(TAG, "Add city to favorites")
        viewModelScope.launch {
            _currentWeather?.let { weather ->
                val currentWeather = weather.deepCopy()
                val request = AddCityUseCase.AddRequest(currentWeather.toDomain())
                _addCityUseCase(request).collect { response ->
                    when (response) {
                        is Response.Success -> {
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