package com.lichle.weather.view.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lichle.weather.view.navigation.DestinationsArgs.CITY_ID_ARG
import com.lichle.weather.view.navigation.DestinationsArgs.CITY_NAME_ARG

/**
 * Arguments used in [Destinations] routes
 */
object DestinationsArgs {

    const val CITY_NAME_ARG = "city_name"
    const val CITY_ID_ARG = "city_id"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val FAVORITE_ROUTE = "favorite"
    const val WEATHER_ROUTE_BY_NAME = "weatherByName/{$CITY_NAME_ARG}"
    const val WEATHER_ROUTE_BY_ID = "weatherById/{$CITY_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

    fun navigateToWeather(cityName: String) {
        navController.navigate("weather/$cityName") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToFavorite() {
        navController.navigate(Destinations.FAVORITE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}