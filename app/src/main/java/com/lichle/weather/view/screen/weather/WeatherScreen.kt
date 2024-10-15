package com.lichle.weather.view.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lichle.weather.R
import com.lichle.weather.domain.Response
import com.lichle.weather.view.ui_common.ResponsiveSnackBar

@Composable
fun WeatherScreen(
    cityName: String? = null,
    onBack: (() -> Unit)? = null,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState by viewModel.weather.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var currentCityName by remember { mutableStateOf(cityName) }
    var showNoInfo by remember { mutableStateOf(cityName == null) }

    LaunchedEffect(Unit) {
        cityName?.let { viewModel.fetchDeviceByName(it) }
    }

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                onBack = onBack,
                onSearchClick = { city ->
                    currentCityName = city
                    showNoInfo = false
                    viewModel.fetchDeviceByName(city)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            when {
                showNoInfo -> EmptyContent(Modifier.padding(innerPadding))
                weatherState is Response.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp
                        )
                    }
                }

                weatherState is Response.Success -> {
                    val weather = (weatherState as Response.Success<WeatherUiModel?>).data
                    if (weather != null) {
                        WeatherDetails(weather, modifier = Modifier.padding(innerPadding))
                    } else {
                        showNoInfo = true
                    }
                }

                weatherState is Response.Error -> {
                    EmptyContent(Modifier.padding(innerPadding))

                    val errorMessage = (weatherState as Response.Error).message
                    ResponsiveSnackBar(
                        snackbarHostState = snackbarHostState,
                        message = context.getString(R.string.error_message, errorMessage),
                        actionLabel = context.getString(R.string.dismiss_action),
                        onDismiss = onBack
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun WeatherScreenPreview() {
    WeatherScreen(cityName = "Ho Chi Minh")
}