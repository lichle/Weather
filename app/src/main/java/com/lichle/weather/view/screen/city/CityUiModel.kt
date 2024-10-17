package com.lichle.weather.view.screen.city

import com.lichle.weather.domain.Weather

data class CityUiModel(
    val id: Int,
    val name: String,
    val country: String
)

fun Weather.toCityUiModel(): CityUiModel {
    return CityUiModel(
        id = id,
        name = name,
        country = country,
    )
}