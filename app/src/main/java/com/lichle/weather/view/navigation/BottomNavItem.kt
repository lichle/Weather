package com.lichle.weather.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Favorite : BottomNavItem(
        route = Destinations.FAVORITE_ROUTE,
        icon = Icons.Default.Favorite,
        title = "Favorite"
    )

    data object Weather : BottomNavItem(
        route = Destinations.WEATHER_ROUTE_BY_NAME, // Use a default value or handle it in navigation
        icon = Icons.Default.WbSunny,
        title = "Weather"
    )
}