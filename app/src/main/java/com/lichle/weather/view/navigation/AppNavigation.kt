package com.lichle.weather.view.navigation

import androidx.navigation.NavHostController
import com.lichle.weather.view.navigation.DestinationsArgs.CITY_ID_ARG
import com.lichle.weather.view.navigation.Screens.CITY_SCREEN
import com.lichle.weather.view.navigation.Screens.WEATHERS_SCREEN

/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val WEATHERS_SCREEN = "weather"
    const val CITY_SCREEN = "city"
}

/**
 * Arguments used in [Destinations] routes
 */
object DestinationsArgs {
    const val DEVICE_ID_ARG = "deviceId"

    const val CITY_ID_ARG = "cityId"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val CITY_ROUTE = CITY_SCREEN
    const val WEATHERS_ROUTE = "${WEATHERS_SCREEN}/{$CITY_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

//    fun navigateToDevices() {
//        navController.navigate(DEVICES_SCREEN)
//    }
//
//    fun navigateToDeviceDetail(deviceId: String) {
//        navController.navigate("$DEVICE_DETAIL_SCREEN/$deviceId")
//    }

}