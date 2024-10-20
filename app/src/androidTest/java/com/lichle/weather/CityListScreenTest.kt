package com.lichle.weather

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.lichle.weather.setup.data.FakeCityRepository
import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.City
import com.lichle.weather.domain.WeatherSummary
import com.lichle.weather.view.screen.city.CityListScreen
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
class CityListScreenTest {

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
        _navController = TestNavHostController(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun `show list when has data`() = runTest {
        // Add the fake weather data
        fakeRepository.addCity(_fakeHueWeather)
        fakeRepository.addCity(_fakeHaNoiWeather)

        // Verify that the weather was added
        val addedHueWeather = fakeRepository.getCity(_fakeHueWeather.id)
        assert(addedHueWeather != null) { "Failed to add weather to repository" }

        val addedHaNoiWeather = fakeRepository.getCity(_fakeHaNoiWeather.id)
        assert(addedHaNoiWeather != null) { "Failed to add weather to repository" }

        composeTestRule.setContent {
            CityListScreen()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("CityList").assertExists()
        composeTestRule.onNodeWithTag("EmptyContent").assertDoesNotExist()
    }

    @Test
    fun `show empty content when no data`() {
        composeTestRule.setContent {
            CityListScreen()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("CityList").assertDoesNotExist()
        composeTestRule.onNodeWithTag("EmptyContent").assertExists()
    }

    @Test
    fun `remove city when click delete button`() = runTest {
        // Set up the UI with the FavoriteScreen and a city in the list
        fakeRepository.addCity(_fakeHueWeather)
        // Verify that the weather was added
        val addedHueWeather = fakeRepository.getCity(_fakeHueWeather.id)
        assert(addedHueWeather != null) { "Failed to add weather to repository" }
        composeTestRule.setContent {
            CityListScreen()
        }

        composeTestRule.onNodeWithTag("BtnDelete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("CityItem").assertDoesNotExist()
    }

    private val _fakeHueWeather = City(
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

    private val _fakeHaNoiWeather = City(
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