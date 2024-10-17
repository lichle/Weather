package com.lichle.weather.view.screen.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lichle.weather.R
import com.lichle.weather.view.ui_common.EmptyContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavHostController,
    cityName: String? = null,
    cityId: Int = 0,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(WeatherIntent.SearchWeather(cityName, cityId))
    }

    Scaffold(topBar = {
        WeatherDetailTopAppBar(navController = navController)
    }, floatingActionButton = {
        if (state is WeatherState.FetchWeatherDataSuccess) {
            val isFavorite = (state as WeatherState.FetchWeatherDataSuccess).isFavorite
            if (!isFavorite) {
                FloatingActionButton(modifier = Modifier.testTag("btnAdd"), onClick = { viewModel.processIntent(WeatherIntent.AddWeather) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_button)
                    )
                }
            }
        }
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is WeatherState.Empty -> {
                    EmptyContent(
                        stringResource(id = R.string.no_weather_data)
                    )
                    (state as? WeatherState.Empty)?.error?.let { error ->
                        LaunchedEffect(error) {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.error_message, error.message),
                                actionLabel = context.getString(R.string.dismiss_action),
                                duration = SnackbarDuration.Indefinite
                            ).let { result ->
                                if (result == SnackbarResult.Dismissed) {
                                    viewModel.processIntent(WeatherIntent.DismissError)
                                }
                            }
                        }
                    }
                }

                is WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center), strokeWidth = 4.dp
                    )
                }

                is WeatherState.FetchWeatherDataSuccess -> {
                    val weather = (state as WeatherState.FetchWeatherDataSuccess).weather
                    WeatherDetails(weather)
                }

                is WeatherState.AddToFavoritesSuccess -> {
                    val weather = (state as WeatherState.AddToFavoritesSuccess).weather
                    WeatherDetails(weather)
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.city_added_to_favorite_list),
                            duration = SnackbarDuration.Short
                        )
                    }
                }

            }
        }
    }
}




