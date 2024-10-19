package com.lichle.weather

import androidx.lifecycle.SavedStateHandle
import com.lichle.core_common.ErrorCodes
import com.lichle.weather.domain.Response
import com.lichle.weather.domain.city.AddCityUseCase
import com.lichle.weather.domain.city.FetchCityUserCase
import com.lichle.weather.domain.city.GetCityUseCase
import com.lichle.weather.domain.city.SearchCityUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.lichle.weather.setup.data.getMockCity
import com.lichle.weather.setup.data.getMockWeatherDto
import com.lichle.weather.view.screen.weather.WeatherIntent
import com.lichle.weather.view.screen.weather.WeatherViewModel
import com.google.common.truth.Truth.assertThat
import com.lichle.weather.setup.MainCoroutineRule

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    // Set the main coroutines dispatcher for unit testing
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Use mocked use cases to be injected into the ViewModel
    private lateinit var _searchCityUseCase: SearchCityUseCase
    private lateinit var _addCityUseCase: AddCityUseCase
    private lateinit var _getCityUseCase: GetCityUseCase
    private lateinit var _fetchCityUseCase: FetchCityUserCase

    // Subject under test
    private lateinit var _weatherViewModel: WeatherViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    // Sample weather data
    private val sampleWeatherUiModel = getMockWeatherDto()

    @Before
    fun setupViewModel() {
        _searchCityUseCase = mock()
        _addCityUseCase = mock()
        _getCityUseCase = mock()
        _fetchCityUseCase = mock()
        savedStateHandle = SavedStateHandle()

        _weatherViewModel = WeatherViewModel(
            savedStateHandle,
            _searchCityUseCase,
            _addCityUseCase,
            _getCityUseCase,
            _fetchCityUseCase
        )
    }

    @Test
    fun `search weather by city name success`() = runTest {
        // Given a successful response from search use case
        val city = getMockCity(1, "Hue")
        whenever(_searchCityUseCase(any())).thenReturn(
            flow { emit(Response.Success(city)) }
        )
        whenever(_getCityUseCase(any())).thenReturn(
            flow { emit(Response.Error(ErrorCodes.NOT_FOUND, "Not found")) }
        )

        // When searching for weather
        _weatherViewModel.processIntent(WeatherIntent.SearchWeather(cityName = "London"))

        // Then verify the state is updated correctly
        val uiState = _weatherViewModel.state.first()
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.weather).isNotNull()
        assertThat(uiState.weather?.name).isEqualTo("Hue")
        assertThat(uiState.isFavorite).isFalse()
    }

    @Test
    fun `search weather by city id success`() = runTest {
        // Given a successful response from fetch use case
        val city = getMockCity(1, "Hue")
        whenever(_fetchCityUseCase(any())).thenReturn(
            flow { emit(Response.Success(city)) }
        )

        // When fetching weather by ID
        _weatherViewModel.processIntent(WeatherIntent.SearchWeather(cityId = 1))

        // Then verify the state
        val uiState = _weatherViewModel.state.first()
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.weather).isNotNull()
        assertThat(uiState.weather?.name).isEqualTo("Hue")
        assertThat(uiState.isFavorite).isTrue()
    }

    @Test
    fun `add city to favorites success`() = runTest {
        // Given a successful search and add response
        val city = getMockCity(1, "Hue")
        whenever(_searchCityUseCase(any())).thenReturn(
            flow { emit(Response.Success(city)) }
        )
        whenever(_addCityUseCase(any())).thenReturn(
            flow { emit(Response.Success(Unit)) }
        )
        whenever(_getCityUseCase(any())).thenReturn(
            flow { emit(Response.Success(city)) }
        )

        // When searching and adding to favorites
        _weatherViewModel.processIntent(WeatherIntent.SearchWeather(cityName = "London"))
        _weatherViewModel.processIntent(WeatherIntent.AddToFavorites)

        // Then verify the state
        val uiState = _weatherViewModel.state.first()
        assertThat(uiState.weather).isNotNull()
        assertThat(uiState.isFavorite).isTrue()
    }

    @Test
    fun `search weather with error`() = runTest {
        // Given an error response from search use case
        whenever(_searchCityUseCase(any())).thenReturn(
            flow { emit(Response.Error(ErrorCodes.NOT_FOUND, "City not found")) }
        )

        // When searching for weather
        _weatherViewModel.processIntent(WeatherIntent.SearchWeather(cityName = "NonexistentCity"))

        // Then verify error state
        val uiState = _weatherViewModel.state.first()
        assertThat(uiState.isLoading).isFalse()
        assertThat(uiState.error?.code).isEqualTo(ErrorCodes.NOT_FOUND)
    }

    @Test
    fun `empty state when no search parameters`() = runTest {
        // When searching without parameters
        _weatherViewModel.processIntent(WeatherIntent.SearchWeather())

        // Then verify empty state
        val uiState = _weatherViewModel.state.first()
        assertThat(uiState.weather).isNull()
        assertThat(uiState.isLoading).isFalse()
    }
}