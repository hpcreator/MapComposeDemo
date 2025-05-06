package com.hpcreation.mapComposeDemo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.maps.android.compose.MapType
import com.hpcreation.mapComposeDemo.Screen
import com.hpcreation.mapComposeDemo.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    features: List<Pair<String, String>>
) {
    val context = LocalContext.current
    val mapViewModel: MapViewModel = koinViewModel()

    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, paddingValues, features)
        }

        composable(
            route = Screen.MapScreen.ROUTE_PATTERN,
            arguments = listOf(
                navArgument("mapType") { type = NavType.StringType },
                navArgument("zoomControlsEnabled") { type = NavType.BoolType },
                navArgument("gestureEnabled") { type = NavType.BoolType })
        ) { backStackEntry ->
            val mapType = runCatching {
                MapType.valueOf(backStackEntry.arguments?.getString("mapType") ?: "")
            }.getOrDefault(MapType.NORMAL)
            val zoomControlsEnabled =
                backStackEntry.arguments?.getBoolean("zoomControlsEnabled") != false
            val gestureEnabled = backStackEntry.arguments?.getBoolean("gestureEnabled") != false

            val coroutineScope = rememberCoroutineScope()
            MapScreen(
                markers = mapViewModel.markers,
                mapType = mapType,
                zoomControlsEnabled = zoomControlsEnabled,
                gestureEnabled = gestureEnabled,
                dynamicMarkers = mapViewModel.markerList,
                onMapReady = {
                    Toast.makeText(context, "Map is ready", Toast.LENGTH_LONG).show()
                },
                onMapLongPressed = { latLng ->
                    coroutineScope.launch {
                        mapViewModel.onMapLongPressed(latLng, context)
                    }
                })
        }

        composable(Screen.PolyLinePolyGon.route) {
            PolylinePolygonScreen()
        }

        composable(Screen.DirectionsDistance.route) {
            DirectionScreen(paddingValues)
        }
        composable(Screen.TravelMode.route) {
            TravelModeScreen(paddingValues)
        }
        composable(Screen.MapStyling.route) { MapStylingScreen(paddingValues) }
        composable(Screen.PlacesGeocoding.route) { AdvancedDataHandlingScreen(paddingValues) }
        composable(Screen.OfflineMaps.route) { OfflineMapsScreen(paddingValues) }
    }
}