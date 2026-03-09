package com.rahul.ticketek.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rahul.ticketek.ui.loading.LoadingScreen
import com.rahul.ticketek.ui.permission.LocationPermissionScreen
import com.rahul.ticketek.ui.result.ResultScreen
import com.rahul.ticketek.ui.scan.ScanTicketScreen
import com.rahul.ticketek.ui.scan.ScanViewModel
import com.rahul.ticketek.ui.splash.SplashScreen
import com.rahul.ticketek.ui.venues.VenuesListScreen
import com.rahul.ticketek.ui.venues.VenuesViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToPermission = {
                    navController.navigate(Screen.LocationPermission.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.LocationPermission.route) {
            LocationPermissionScreen(
                onPermissionGranted = {
                    navController.navigate(Screen.VenuesFlow.route) {
                        popUpTo(Screen.LocationPermission.route) { inclusive = true }
                    }
                }
            )
        }

        navigation(
            route = Screen.VenuesFlow.route,
            startDestination = Screen.Loading.route
        ) {
            composable(Screen.Loading.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.VenuesFlow.route)
                }
                val venuesViewModel: VenuesViewModel =
                    hiltViewModel(viewModelStoreOwner = parentEntry)
                val uiState by venuesViewModel.uiState.collectAsState()

                LaunchedEffect(uiState.isLoading) {
                    if (!uiState.isLoading) {
                        navController.navigate(Screen.VenuesList.route) {
                            popUpTo(Screen.Loading.route) { inclusive = true }
                        }
                    }
                }

                LoadingScreen("Finding nearby venues...")
            }

            composable(Screen.VenuesList.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.VenuesFlow.route)
                }
                val venuesViewModel: VenuesViewModel =
                    hiltViewModel(viewModelStoreOwner = parentEntry)

                VenuesListScreen(
                    onVenueSelected = { venue ->
                        navController.navigate(
                            Screen.ScanTicket.createRoute(
                                venueCode = venue.venueCode,
                                venueName = venue.venueName
                            )
                        )
                    },
                    viewModel = venuesViewModel
                )
            }
        }

        composable(
            route = Screen.ScanTicket.route,
            arguments = listOf(
                navArgument("venueCode") { type = NavType.StringType },
                navArgument("venueName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val venueCode = backStackEntry.arguments?.getString("venueCode").orEmpty()
            val venueName = backStackEntry.arguments?.getString("venueName").orEmpty()

            ScanTicketScreen(
                venueName = venueName,
                venueCode = venueCode,
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.VenuesList.route,
                        inclusive = false
                    )
                },
                onScanResult = {
                    navController.navigate(Screen.Result.route)
                }
            )
        }

        composable(Screen.Result.route) { backStackEntry ->
            val scanBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.ScanTicket.route)
            }
            val scanViewModel: ScanViewModel =
                hiltViewModel(viewModelStoreOwner = scanBackStackEntry)

            ResultScreen(
                onResumeScanning = {
                    navController.popBackStack(
                        route = Screen.ScanTicket.route,
                        inclusive = false
                    )
                },
                viewModel = scanViewModel
            )
        }
    }
}
