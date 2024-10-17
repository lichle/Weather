package com.lichle.weather.screen

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.lichle.weather.MainActivity
import com.lichle.weather.data.FakeWeatherRepository
import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.domain.Weather
import com.lichle.weather.domain.WeatherSummary
import com.lichle.weather.view.screen.favorite.FavoriteScreen
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
class FavoriteScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val fakeRepository: WeatherRepository = FakeWeatherRepository()

    private lateinit var _navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        _navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun hasData_DisplayList() = runTest {
        // Add the fake weather data
        fakeRepository.addWeather(_fakeHueWeather)
        fakeRepository.addWeather(_fakeHaNoiWeather)

        // Verify that the weather was added
        val addedHueWeather = fakeRepository.getWeather(_fakeHueWeather.id)
        assert(addedHueWeather != null) { "Failed to add weather to repository" }

        val addedHaNoiWeather = fakeRepository.getWeather(_fakeHaNoiWeather.id)
        assert(addedHaNoiWeather != null) { "Failed to add weather to repository" }

        composeTestRule.activity.setContent {
            FavoriteScreen(navController = _navController)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("FavoriteList").assertExists()
        composeTestRule.onNodeWithTag("EmptyContent").assertDoesNotExist()
    }

    @Test
    fun noData_DisplayEmptyContent() {
        composeTestRule.activity.setContent {
            FavoriteScreen(navController = _navController)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("FavoriteList").assertDoesNotExist()
        composeTestRule.onNodeWithTag("EmptyContent").assertExists()
    }

    @Test
    fun deleteButton_RemovesFavoriteItem() = runTest {
        // Set up the UI with the FavoriteScreen and a city in the list
        fakeRepository.addWeather(_fakeHueWeather)
        // Verify that the weather was added
        val addedHueWeather = fakeRepository.getWeather(_fakeHueWeather.id)
        assert(addedHueWeather != null) { "Failed to add weather to repository" }
        composeTestRule.activity.setContent {
            FavoriteScreen(
                navController = _navController,
            )
        }

        composeTestRule.onNodeWithTag("BtnDelete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("FavoriteItem").assertDoesNotExist()
    }

    private val _fakeHueWeather = Weather(
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

    private val _fakeHaNoiWeather = Weather(
        id = 1581130,
        name = "Hanoi",
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