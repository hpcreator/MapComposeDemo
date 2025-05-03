package com.hpcreation.mapComposeDemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.maps.android.compose.MapType
import com.hpcreation.mapComposeDemo.ui.screens.DirectionScreen
import com.hpcreation.mapComposeDemo.ui.screens.HomeScreen
import com.hpcreation.mapComposeDemo.ui.screens.MapScreen
import com.hpcreation.mapComposeDemo.ui.screens.PolylinePolygonScreen
import com.hpcreation.mapComposeDemo.ui.theme.MapComposeDemoTheme
import com.hpcreation.mapComposeDemo.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val features = listOf(
            "Previous Demo" to Screen.MapScreen.routeWithArgs(
                mapType = MapType.NORMAL, zoomControlsEnabled = true, gestureEnabled = false
            ),
            "Polyline and Polygon" to Screen.PolyLinePolyGon.route,
            "Directions and Distance" to Screen.DirectionsDistance.route,
            "Different Travel Mode" to Screen.TravelMode.route,
            "Map Styling" to Screen.MapStyling.route,
            "Advanced Data Handling" to Screen.PlacesGeocoding.route,
            "Clustering Markers" to Screen.Clustering.route,
            "Geofencing" to Screen.Geofencing.route,
            "Offline Maps and Caching" to Screen.OfflineMaps.route,
            "Performance Optimization" to Screen.Performance.route,
            "Security and Accessibility" to Screen.SecurityAccessibility.route
        )

        setContent {
            val navController = rememberNavController()

            MapComposeDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val showBackButton = currentDestination?.route != Screen.Home.route

                        val toolbarTitle = features.find {
                            currentDestination?.route?.startsWith(it.second.take(10)) == true
                        }?.first ?: "Google Map Demo"
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            ), title = {
                                Text(toolbarTitle)
                            }, navigationIcon = {
                                if (showBackButton) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            })
                    }) { innerPadding ->
                    AppNavigation(navController, innerPadding, features)
                }

            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    features: List<Pair<String, String>>
) {
    val mapViewModel: MapViewModel = koinViewModel()
    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { HomeScreen(navController, paddingValues, features) }

        composable(
            route = Screen.MapScreen.ROUTE_PATTERN,
            arguments = listOf(
                navArgument("mapType") { type = NavType.StringType },
                navArgument("zoomControlsEnabled") { type = NavType.BoolType },
                navArgument("gestureEnabled") { type = NavType.BoolType })
        ) { backStackEntry ->
            val context = LocalContext.current
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
        composable(Screen.PolyLinePolyGon.route) { PolylinePolygonScreen() }
        composable(Screen.DirectionsDistance.route) { DirectionScreen(paddingValues) }
    }
}
