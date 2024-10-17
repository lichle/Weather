//package com.lichle.weather
//
//import com.lichle.weather.domain.Response
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.kotlin.mock
//import com.google.common.truth.Truth.assertThat
//import com.lichle.core_common.ErrorCodes
//import com.lichle.weather.domain.NoRequest
//import com.lichle.weather.domain.city.DeleteCityUseCase
//import com.lichle.weather.domain.city.GetCityListUseCase
//import com.lichle.weather.setup.MainCoroutineRule
//import com.lichle.weather.view.screen.city.CityUiModel
//import com.lichle.weather.view.screen.city.FavoriteIntent
//import com.lichle.weather.view.screen.city.FavoriteState
//import com.lichle.weather.view.screen.city.FavoriteViewModel
//import kotlinx.coroutines.test.advanceUntilIdle
//
//@ExperimentalCoroutinesApi
//class FavoriteViewModelTest {
//
//    // Subject under test
//    private lateinit var favoriteViewModel: FavoriteViewModel
//
//    // Mock use cases
//    private lateinit var _getCityListUseCase: FakeGetCityListUseCase
//    private lateinit var _deleteCityUseCase: FakeDeleteCityUseCase
//
//    // Set the main coroutines dispatcher for unit testing
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()
//
//    @Before
//    fun setupViewModel() {
//        _getCityListUseCase = mock()
//        _deleteCityUseCase = mock()
//
//        favoriteViewModel = FavoriteViewModel(
//            _getCityListUseCase = _getCityListUseCase,
//            _deleteCityUseCase = _deleteCityUseCase
//        )
//    }
//
//    @Test
//    fun `loadFavorites success - loading shown and data loaded correctly`() = runTest {
//        // Given
//        val cities = listOf(
//            CityUiModel(1, "New York", "US"),
//            CityUiModel(2, "London", "UK"),
//            CityUiModel(3, "Tokyo", "JP")
//        )
//        _getCityListUseCase.setCities(cities)
//
//        // When
//        favoriteViewModel.processIntent(FavoriteIntent.LoadFavorites)
//
//        // Then - verify loading state
//        assertThat(favoriteViewModel.state.value).isInstanceOf(FavoriteState.Loading::class.java)
//
//        // Execute pending coroutines
//        advanceUntilIdle()
//
//        // Then - verify success state
//        val currentState = favoriteViewModel.state.value
//        assertThat(currentState).isInstanceOf(FavoriteState.Success::class.java)
//        assertThat((currentState as FavoriteState.Success).cities).hasSize(3)
//        assertThat(currentState.cities).isEqualTo(cities)
//    }
//
//    @Test
//    fun `loadFavorites error - error state shown`() = runTest {
//        // Given
//        val errorMessage = "Failed to load cities"
//        _getCityListUseCase.setError(errorMessage)
//
//        // When
//        favoriteViewModel.processIntent(FavoriteIntent.LoadFavorites)
//
//        // Execute pending coroutines
//        advanceUntilIdle()
//
//        // Then
//        val currentState = favoriteViewModel.state.value
//        assertThat(currentState).isInstanceOf(FavoriteState.Error::class.java)
//        assertThat((currentState as FavoriteState.Error).message).isEqualTo(errorMessage)
//    }
//
//    @Test
//    fun `deleteCity success - city deleted and list reloaded`() = runTest {
//        // Given
//        val cities = listOf(
//            CityUiModel(1, "New York", "US"),
//            CityUiModel(2, "London", "UK")
//        )
//        _getCityListUseCase.setCities(cities)
//
//        // Initial load
//        favoriteViewModel.processIntent(FavoriteIntent.LoadFavorites)
//        advanceUntilIdle()
//
//        // When
//        favoriteViewModel.processIntent(FavoriteIntent.DeleteCity(1))
//        advanceUntilIdle()
//
//        // Then
//        val updatedCities = listOf(CityUiModel(2, "London", "UK"))
//        _getCityListUseCase.setCities(updatedCities)
//
//        val currentState = favoriteViewModel.state.value
//        assertThat(currentState).isInstanceOf(FavoriteState.Success::class.java)
//        assertThat((currentState as FavoriteState.Success).cities).hasSize(1)
//        assertThat(currentState.cities).isEqualTo(updatedCities)
//    }
//
//    @Test
//    fun `deleteCity error - error state shown`() = runTest {
//        // Given
//        val errorMessage = "Failed to delete city"
//        _deleteCityUseCase.setError(errorMessage)
//
//        // When
//        favoriteViewModel.processIntent(FavoriteIntent.DeleteCity(1))
//        advanceUntilIdle()
//
//        // Then
//        val currentState = favoriteViewModel.state.value
//        assertThat(currentState).isInstanceOf(FavoriteState.Error::class.java)
//        assertThat((currentState as FavoriteState.Error).message).contains(errorMessage)
//    }
//}
//
//// Fake implementation of GetCityListUseCase for testing
//class FakeGetCityListUseCase {
//    private var cities: List<CityUiModel> = emptyList()
//    private var shouldError = false
//    private var errorMessage = ""
//
//    fun setCities(newCities: List<CityUiModel>) {
//        cities = newCities
//    }
//
//    fun setError(message: String) {
//        shouldError = true
//        errorMessage = message
//    }
//
//    operator fun invoke(request: NoRequest) = flow {
//        emit(Response.Loading)
//        if (shouldError) {
//            emit(Response.Error(ErrorCodes.UNKNOWN, "Error"))
//        } else {
//            emit(Response.Success(cities))
//        }
//    }
//}
//
//// Fake implementation of DeleteCityUseCase for testing
//class FakeDeleteCityUseCase {
//    private var shouldError = false
//    private var errorMessage = ""
//
//    fun setError(message: String) {
//        shouldError = true
//        errorMessage = message
//    }
//
//    operator fun invoke(request: DeleteCityUseCase.DeleteRequest) = flow {
//        emit(Response.Loading)
//        if (shouldError) {
//            emit(Response.Error(ErrorCodes.UNKNOWN,"Error"))
//        } else {
//            emit(Response.Success(Unit))
//        }
//    }
//}