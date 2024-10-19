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

    fun navigateToWeatherByCityName(name: String) {
        navController.navigate("weatherByName/$name")
    }
    fun navigateToWeatherByCityId(id: Int) {
        navController.navigate("weatherById/$id")
    }

}