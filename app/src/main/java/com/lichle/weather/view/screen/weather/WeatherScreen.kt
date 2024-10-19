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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.lichle.weather.R
import com.lichle.weather.view.ui_common.EmptyContent
import com.lichle.weather.view.ui_common.asString

@Composable
fun WeatherScreen(
    navController: NavHostController,
    cityName: String? = null,
    cityId: Int = 0,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is WeatherUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }

                WeatherUiEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(WeatherIntent.SearchWeather(cityName, cityId))
    }

    Scaffold(
        topBar = {
            WeatherDetailTopAppBar(navController = navController)
        },
        floatingActionButton = {
            if (uiState.weather != null && !uiState.isFavorite) {
                FloatingActionButton(
                    modifier = Modifier.testTag("btnAdd"),
                    onClick = { viewModel.processIntent(WeatherIntent.AddToFavorites) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_button)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        strokeWidth = 4.dp
                    )
                }

                uiState.weather != null -> {
                    WeatherDetails(uiState.weather!!)
                }

                else -> {
                    EmptyContent(stringResource(id = R.string.no_weather_data))
                }
            }

            // Show error if present
            uiState.error?.let { error ->
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
    }
}




