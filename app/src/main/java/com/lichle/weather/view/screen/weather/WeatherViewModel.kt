package com.lichle.weather.view.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.weather.FetchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor (
    private val fetchWeatherUseCase: FetchWeatherUseCase
): ViewModel() {
    private val _weather = MutableStateFlow<Response<WeatherUiModel?>>(Response.Loading)
    val weather: StateFlow<Response<WeatherUiModel?>> get() = _weather


    fun fetchDeviceByName(cityName: String) {
        Logger.d(TAG, "Fetching weather for city: $cityName")
        viewModelScope.launch {
            fetchWeatherUseCase(cityName).collect { response ->
                _weather.value = when (response) {
                    is Response.Loading -> Response.Loading
                    is Response.Success -> Response.Success(response.data?.toUiModel())
                    is Response.Error -> {
                        Logger.e(TAG, "Error fetching weather: ${response.message}")
                        Response.Error(response.code, response.message)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = WeatherViewModel::class.java.simpleName
    }
}