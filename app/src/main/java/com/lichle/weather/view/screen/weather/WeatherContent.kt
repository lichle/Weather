package com.lichle.weather.view.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lichle.weather.R

@Composable
fun WeatherDetails(weather: WeatherUiModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CityText(text = stringResource(R.string.city_label, weather.name))
        WeatherInfoText(text = stringResource(R.string.country_label, weather.country))
        WeatherInfoText(text = stringResource(R.string.temperature_label, weather.temperature))
        WeatherInfoText(text = stringResource(R.string.feels_like_label, weather.feelsLike))
        WeatherInfoText(text = stringResource(R.string.humidity_label, weather.humidity))
        WeatherInfoText(text = stringResource(R.string.wind_speed_label, weather.windSpeed))
        WeatherInfoText(text = stringResource(R.string.cloudiness_label, weather.cloudiness))

        // Weather conditions (e.g., "Clear Sky")
        weather.weather.forEach { condition ->
            WeatherInfoText(
                text = stringResource(
                    R.string.condition_label,
                    condition.main,
                    condition.description
                )
            )
        }
    }
}

@Composable
fun WeatherInfoText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun CityText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_information),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}