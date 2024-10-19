package com.lichle.weather.view.screen.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.core_common.ErrorCodes
import com.lichle.weather.R
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.City
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.AddCityUseCase
import com.lichle.weather.domain.city.FetchCityUserCase
import com.lichle.weather.domain.city.GetCityUseCase
import com.lichle.weather.domain.city.SearchCityUseCase
import com.lichle.weather.view.ui_common.EventHandler
import com.lichle.weather.view.ui_common.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

data class WeatherUiState(
    val weather: WeatherUiModel? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: ErrorInfo? = null
)

data class ErrorInfo(val code: Int, val message: String)

sealed class WeatherIntent {
    data class SearchWeather(val cityName: String? = null, val cityId: Int = 0) : WeatherIntent()
    data object AddToFavorites : WeatherIntent()
    data object DismissError : WeatherIntent()
}

sealed class WeatherUiEvent {
    data class ShowSnackbar(val message: StringResource) : WeatherUiEvent()
    data object NavigateBack : WeatherUiEvent()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Named("SearchCityUseCase") private val _searchCityUseCase: BaseUseCase<SearchCityUseCase.SearchRequest, City?>,
    @Named("AddCityUseCase") private val _addCityUseCase: BaseUseCase<AddCityUseCase.AddRequest, Unit>,
    @Named("GetCityUseCase") private val _getCityUseCase: BaseUseCase<GetCityUseCase.GetRequest, City>,
    @Named("FetchCityUserCase") private val _fetchCityUseCase: BaseUseCase<FetchCityUserCase.FetchRequest, City>
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    private val _eventHandler = EventHandler<WeatherUiEvent>()
    val events = _eventHandler.events

    fun processIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.SearchWeather -> {
                if (_state.value.weather == null) {
                    searchWeatherByCityOrId(intent.cityName, intent.cityId)
                }
            }

            is WeatherIntent.AddToFavorites -> addToFavorites()
            is WeatherIntent.DismissError -> dismissError()
        }
    }

    private fun searchWeatherByCityOrId(cityName: String?, cityId: Int) {
        if (cityName == null && cityId == 0) {
            val savedWeather = savedStateHandle.get<WeatherUiModel>(SAVED_WEATHER_KEY)
            if (savedWeather != null) {
                _state.value = WeatherUiState(weather = savedWeather, isFavorite = true)
            }
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when {
                !cityName.isNullOrBlank() -> searchWeatherByCity(cityName)
                cityId != 0 -> fetchWeather(cityId)
                else -> _state.value = _state.value.copy(
                    isLoading = false,
                    error = ErrorInfo(ErrorCodes.NOT_FOUND, "No search criteria provided")
                )
            }
        }
    }

    private suspend fun searchWeatherByCity(cityName: String) {
        viewModelScope.launch {
            val request = SearchCityUseCase.SearchRequest(cityName)
            _searchCityUseCase(request).collect { response ->
                when (response) {
                    is Response.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Response.Success -> {
                        response.data?.let { weatherDto ->
                            val weather = weatherDto.toUiModel()
                            savedStateHandle[SAVED_WEATHER_KEY] = weather
                            checkFavoriteStatus(weather)
                        } ?: run {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = ErrorInfo(ErrorCodes.NOT_FOUND, "Weather data not found")
                            )
                        }
                    }

                    is Response.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = ErrorInfo(response.code, response.message)
                        )
                    }
                }
            }
        }
    }

    private suspend fun checkFavoriteStatus(weather: WeatherUiModel) {
        viewModelScope.launch {
            val request = GetCityUseCase.GetRequest(weather.id)
            _getCityUseCase(request).collect { response ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    weather = weather,
                    isFavorite = response is Response.Success,
                    error = null
                )
            }
        }
    }

    private fun addToFavorites() {
        viewModelScope.launch {
            val currentWeather = _state.value.weather ?: return@launch
            val request = AddCityUseCase.AddRequest(currentWeather.toDomain())

            _addCityUseCase(request).collect { response ->
                when (response) {
                    is Response.Success -> {
                        _state.value = _state.value.copy(isFavorite = true)
                        _eventHandler.emitEvent(
                            WeatherUiEvent.ShowSnackbar(
                                StringResource.Resource(R.string.city_added_to_favorite_list)
                            )
                        )
                    }

                    is Response.Error -> {
                        _eventHandler.emitEvent(
                            WeatherUiEvent.ShowSnackbar(
                                StringResource.Plain(response.message)
                            )
                        )
                    }

                    is Response.Loading -> {}
                }
            }
        }
    }

    private fun dismissError() {
        _state.value = _state.value.copy(error = null)
    }

    private suspend fun fetchWeather(id: Int) {
        Logger.d(TAG, "Fetch weather by id: $id")
        viewModelScope.launch {
            val request = FetchCityUserCase.FetchRequest(id)
            _fetchCityUseCase(request).collect { response ->
                when (response) {
                    is Response.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Response.Success -> {
                        val weather = response.data.toUiModel()
                        _state.value = _state.value.copy(
                            isLoading = false,
                            weather = weather,
                            isFavorite = true,
                            error = null
                        )
                    }

                    is Response.Error -> {
                        _eventHandler.emitEvent(
                            WeatherUiEvent.ShowSnackbar(
                                StringResource.Plain(response.message)
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "WeatherViewModel"
        private const val SAVED_WEATHER_KEY = "saved_weather"
    }
}