package com.lichle.weather.view.screen.weather

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.lichle.weather.R
import com.lichle.weather.view.theme.appTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailTopAppBar(
    navController: NavHostController,
    title: String = stringResource(id = R.string.weather_detail)
) {
    TopAppBar(
        title = {
            Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = appTopAppBarColors()
    )
}