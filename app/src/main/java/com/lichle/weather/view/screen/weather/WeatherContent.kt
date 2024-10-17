package com.lichle.weather.view.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lichle.weather.R

@Composable
internal fun WeatherDetails(weather: WeatherUiModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .testTag("WeatherDetails")
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CityText(text = stringResource(R.string.city_label, weather.name))
        WeatherInfoText(text = stringResource(R.string.country_label, weather.country))
        WeatherInfoText(text = stringResource(R.string.temperature_label, weather.temperature))
        WeatherInfoText(text = stringResource(R.string.feels_like_label, weather.feelsLike))
        WeatherInfoText(text = stringResource(R.string.humidity_label, weather.humidity))
        WeatherInfoText(text = stringResource(R.string.wind_speed_label, weather.windSpeed))
        WeatherInfoText(text = stringResource(R.string.cloudiness_label, weather.cloudiness))

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
fun CityText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.fillMaxWidth(),
        textAlign = textAlign
    )
}

@Composable
fun WeatherInfoText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth(),
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}