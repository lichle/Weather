package com.lichle.weather

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.lichle.weather.setup.data.FakeCityRepository
import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.City
import com.lichle.weather.domain.WeatherSummary
import com.lichle.weather.view.screen.weather.WeatherScreen
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
class WeatherScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue
    @JvmField
    val fakeRepository: CityRepository = FakeCityRepository()

    private lateinit var _navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        _navController =
            TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun hasData_DisplayWeatherDetail() = runTest {
        // Add the fake weather data
        fakeRepository.addCity(_fakeWeather)

        // Verify that the weather was added
        val addedWeather = fakeRepository.getCity(_fakeWeather.id)
        assert(addedWeather != null) { "Failed to add weather to repository" }

        //Show Hue weather
        val cityName = "Hue"
        composeTestRule.setContent {
            WeatherScreen(navController = _navController, cityName = cityName)
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("WeatherDetails").assertExists()
        composeTestRule.onNodeWithTag("EmptyContent").assertDoesNotExist()
    }

    @Test
    fun noData_DisplayEmptyContent() {
        composeTestRule.setContent {
            WeatherScreen(navController = _navController)
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("EmptyContent").assertExists()
        composeTestRule.onNodeWithTag("WeatherDetails").assertDoesNotExist()
    }

    @Test
    fun isFavorite_NotDisplayAddButton() = runTest {
        // Add the fake weather data
        fakeRepository.addCity(_fakeWeather)

        // Verify that the weather was added
        val addedWeather = fakeRepository.getCity(_fakeWeather.id)
        assert(addedWeather != null) { "Failed to add weather to repository" }

        //Show Hue weather
        val cityName = "Hue"
        composeTestRule.setContent {
            WeatherScreen(navController = _navController, cityName = cityName)
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("btnAdd").assertDoesNotExist()
    }


    private val _fakeWeather = City(
        id = 1580240,
        name = "Hue",
        lon = 107.6,
        lat = 16.4667,
        temperature = 26.06,
        feelsLike = 26.06,
        tempMin = 26.06,
        tempMax = 26.06,
        pressure = 1012,
        humidity = 89,
        seaLevel = 1012,
        groundLevel = 1012,
        visibility = 10000,
        windSpeed = 1.54,
        windDeg = 190,
        windGust = 15.0,  // No wind gust data provided
        cloudiness = 40,
        weather = listOf(
            WeatherSummary(
                id = 500,
                main = "Rain",
                description = "light rain",
                icon = "10n"
            )
        ),
        country = "VN",
        sunrise = 1729032127,
        sunset = 1729074470,
        timestamp = 1729094143,
        timezone = 25200
    )

}