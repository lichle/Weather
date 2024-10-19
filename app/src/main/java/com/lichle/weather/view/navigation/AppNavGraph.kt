package com.lichle.weather.view.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lichle.weather.view.screen.city.CityListScreen
import com.lichle.weather.view.screen.weather.WeatherScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    Scaffold(
        bottomBar = {
//            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.FAVORITE_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Destinations.FAVORITE_ROUTE,
            ) {
                CityListScreen(
                    onSearchClick = { name ->
                        navActions.navigateToWeatherByCityName(name)
                    },
                    onItemClick = {
                        navActions.navigateToWeatherByCityId(it.id)
                    }
                )
            }

            composable(
                route = Destinations.WEATHER_ROUTE_BY_NAME,
                arguments = listOf(
                    navArgument(DestinationsArgs.CITY_NAME_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString(DestinationsArgs.CITY_NAME_ARG)
                WeatherScreen(navController = navController, cityName = cityName)
            }

            composable(
                route = Destinations.WEATHER_ROUTE_BY_ID,
                arguments = listOf(
                    navArgument(DestinationsArgs.CITY_ID_ARG) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val cityId = backStackEntry.arguments?.getInt(DestinationsArgs.CITY_ID_ARG)
                WeatherScreen(navController = navController, cityId = cityId ?: 0)
            }
        }
    }
}