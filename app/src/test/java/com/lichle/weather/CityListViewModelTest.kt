package com.lichle.weather

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.lichle.weather.domain.BaseUseCase
import com.lichle.weather.domain.City
import com.lichle.weather.domain.NoRequest
import com.lichle.weather.domain.city.DeleteCityUseCase
import com.lichle.weather.setup.MainCoroutineRule
import com.lichle.weather.setup.data.getMockCity
import com.lichle.weather.view.screen.city.CityListIntent
import com.lichle.weather.view.screen.city.CityListUiEvent
import com.lichle.weather.view.screen.city.CityListViewModel
import com.lichle.weather.view.screen.city.toCityUiModel
import com.lichle.weather.view.ui_common.StringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import java.lang.Exception


@ExperimentalCoroutinesApi
class CityListViewModelTest {

    // Subject under test
    private lateinit var _cityListViewModel: CityListViewModel

    // Mock use cases
    private lateinit var _getCityListUseCase: BaseUseCase<NoRequest, List<City>>
    private lateinit var _deleteCityUseCase: BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit>

    // Set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        _getCityListUseCase = FakeGetCityListUseCase()
        _deleteCityUseCase = FakeDeleteCityUseCase()

        _cityListViewModel = CityListViewModel(
            _getCityListUseCase = _getCityListUseCase,
            _deleteCityUseCase = _deleteCityUseCase
        )
    }

    @Test
    fun `loadFavorites success - loading shown and data loaded correctly`() = runTest {
        // Given
        val cities = listOf(
            getMockCity(1, "Hue"),
            getMockCity(2, "Sai Gon"),
            getMockCity(3, "Da Nang")
        )
        (_getCityListUseCase as FakeGetCityListUseCase).setCities(cities)

        // When
        _cityListViewModel.processIntent(CityListIntent.LoadFavorites)

        // Execute pending coroutines
        advanceUntilIdle()

        // Then
        val currentState = _cityListViewModel.state.value
        assertThat(currentState.isLoading).isFalse()
        assertThat(currentState.cities).hasSize(3)
        assertThat(currentState.cities).isEqualTo(cities.map { it.toCityUiModel() })
    }

    @Test
    fun `loadFavorites error - error message shown`() = runTest {
        // Collect the events from the ViewModel
        val events = mutableListOf<CityListUiEvent>()

        // Launch a coroutine to collect events emitted by the ViewModel
        val job = launch {
            _cityListViewModel.events.collect { event ->
                events.add(event)
            }
        }

        // Given
        val errorMessage = "Failed to load cities"
        (_getCityListUseCase as FakeGetCityListUseCase).setError(errorMessage)

        // When
        _cityListViewModel.processIntent(CityListIntent.LoadFavorites)

        // Execute pending coroutines
        advanceUntilIdle()

        // Then
        val currentState = _cityListViewModel.state.value
        assertThat(currentState.isLoading).isFalse()
        assertThat(events).containsExactly(CityListUiEvent.ShowSnackbar(
            StringResource.Plain("An unexpected error occurred: Failed to load cities")
        ))
        job.cancel()
    }

//    @Test
//    fun `deleteCity success`() = runTest {
//        // Given
//        val cities = listOf(
//            getMockCity(1, "Hue"),
//            getMockCity(2, "Ha Noi"),
//        )
//        // Set initial city list
//        (_getCityListUseCase as FakeGetCityListUseCase).setCities(cities)
//
//        // Initial load
//        _cityListViewModel.processIntent(CityListIntent.LoadFavorites)
//        advanceUntilIdle()  // Wait for coroutines to complete
//
//        // Assert the initial state
//        val initialState = _cityListViewModel.state.value
//        assertThat(initialState.isLoading).isFalse()
//        assertThat(initialState.cities).hasSize(2)
//        assertThat(initialState.cities).isEqualTo(cities.map { it.toCityUiModel() })
//
//        // When
//        _cityListViewModel.processIntent(CityListIntent.DeleteCity(1))
//        advanceUntilIdle()  // Wait for the delete process to finish
//
//        // Now simulate the updated city list after deletion
//        val updatedCities = listOf(getMockCity(2, "Ha Noi"))
//        (_getCityListUseCase as FakeGetCityListUseCase).setCities(updatedCities)
//
//        // Then
//        val updatedState = _cityListViewModel.state.value
//        assertThat(updatedState.isLoading).isFalse()
//        assertThat(updatedState.cities).hasSize(1)
//        assertThat(updatedState.cities).isEqualTo(updatedCities.map { it.toCityUiModel() })
//    }
}

// Fake implementation of GetCityListUseCase for testing
class FakeGetCityListUseCase: BaseUseCase<NoRequest, List<City>> {
    private var cities: List<City> = emptyList()
    private var shouldError = false
    private var errorMessage = ""

    fun setCities(newCities: List<City>) {
        cities = newCities
    }

    fun setError(message: String) {
        shouldError = true
        errorMessage = message
    }

    override suspend fun execute(request: NoRequest): Flow<List<City>> = flow {
        if (shouldError) {
            throw Exception(errorMessage)
        }
        emit(cities)
    }
}

// Fake implementation of DeleteCityUseCase for testing
class FakeDeleteCityUseCase: BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit> {
    private var shouldError = false
    private var errorMessage = ""

    fun setError(message: String) {
        shouldError = true
        errorMessage = message
    }

    override suspend fun execute(request: DeleteCityUseCase.DeleteRequest): Flow<Unit> = flow {
        emit(Unit)
    }
}