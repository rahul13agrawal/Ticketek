package com.rahul.ticketek.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object LocationPermission : Screen("location_permission")
    data object VenuesFlow : Screen("venues_flow")
    data object Loading : Screen("loading")
    data object VenuesList : Screen("venues_list")
    data object ScanTicket : Screen("scan_ticket/{venueCode}/{venueName}") {
        fun createRoute(venueCode: String, venueName: String): String {
            return "scan_ticket/${Uri.encode(venueCode)}/${Uri.encode(venueName)}"
        }
    }
    data object Result : Screen("result")
}
