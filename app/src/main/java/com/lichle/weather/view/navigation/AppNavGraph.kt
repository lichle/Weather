package com.lichle.weather.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lichle.weather.view.screen.weather.WeatherScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.WEATHERS_ROUTE  // Set the start destination to DeviceListScreen
    ) {
        composable(Destinations.WEATHERS_ROUTE) {
            WeatherScreen()
        }

    }
}