package com.lichle.weather.view.screen.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichle.weather.R
import com.lichle.weather.common.Logger
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.City
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.DeleteCityUseCase
import com.lichle.weather.view.ui_common.EventHandler
import com.lichle.weather.view.ui_common.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

data class CityListUiState(
    val cities: List<CityUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val showWeather: String = "",
    val errorMessage: StringResource? = null
)

sealed class CityListIntent {
    data object LoadFavorites : CityListIntent()
    data class DeleteCity(val cityId: Int) : CityListIntent()
    data class SearchCityWeather(val cityName: String) : CityListIntent()
}

sealed class CityListUiEvent {
    data class ShowSnackbar(val message: StringResource) : CityListUiEvent()
    data class NavigateToWeather(val cityName: String) : CityListUiEvent()
}

@HiltViewModel
class CityListViewModel @Inject constructor(
    @Named("GetCityListUseCase") private val _getCityListUseCase: BaseUseCase<NoRequest, List<City>>,
    @Named("DeleteCityUseCase") private val _deleteCityUseCase: BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit>
) : ViewModel() {

    private val _state = MutableStateFlow(CityListUiState())
    val state: StateFlow<CityListUiState> = _state.asStateFlow()

    private val _eventHandler = EventHandler<CityListUiEvent>()
    val events = _eventHandler.events

    init {
        processIntent(CityListIntent.LoadFavorites)
    }

    fun processIntent(intent: CityListIntent) {
        Logger.d(TAG, "processIntent $intent")
        when (intent) {
            is CityListIntent.LoadFavorites -> fetchCityList()
            is CityListIntent.DeleteCity -> deleteCityById(intent.cityId)
            is CityListIntent.SearchCityWeather -> searchCityWeather(intent.cityName)
        }
    }

    private fun searchCityWeather(cityName: String) {
        viewModelScope.launch {
            if (cityName.isBlank()) {
                _eventHandler.emitEvent(
                    CityListUiEvent.ShowSnackbar(
                        StringResource.Resource(R.string.error_city_name_blank)
                    )
                )
                return@launch
            }
            _eventHandler.emitEvent(CityListUiEvent.NavigateToWeather(cityName))
        }
    }

    private fun fetchCityList() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _getCityListUseCase(NoRequest).collect { response ->
                when (response) {
                    is Response.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Response.Success -> _state.value = _state.value.copy(
                        isLoading = false,
                        cities = response.data.map { it.toCityUiModel() }
                    )

                    is Response.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                        _eventHandler.emitEvent(
                            CityListUiEvent.ShowSnackbar(
                                StringResource.Plain(response.message)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun deleteCityById(cityId: Int) {
        viewModelScope.launch {
            _deleteCityUseCase(DeleteCityUseCase.DeleteRequest(cityId)).collect { response ->
                when (response) {
                    is Response.Success -> processIntent(CityListIntent.LoadFavorites)
                    is Response.Error -> {
                        _eventHandler.emitEvent(
                            CityListUiEvent.ShowSnackbar(
                                StringResource.ResourceFormat(
                                    R.string.error_delete_city,
                                    response.message
                                )
                            )
                        )
                    }

                    Response.Loading -> {}
                }
            }
        }
    }

    companion object {
        private val TAG = CityListViewModel::class.java.simpleName
    }
}