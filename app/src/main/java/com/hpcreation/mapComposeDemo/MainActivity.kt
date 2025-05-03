package com.hpcreation.mapComposeDemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.MapType
import com.hpcreation.mapComposeDemo.ui.screens.NavigationHost
import com.hpcreation.mapComposeDemo.ui.theme.MapComposeDemoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val features = getFeatures()

            MapComposeDemoTheme {
                MainScreen(navController, features)
            }
        }
    }

    private fun getFeatures(): List<Pair<String, String>> {
        return listOf(
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, features: List<Pair<String, String>>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBackButton = currentDestination?.route != Screen.Home.route

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text(getToolbarTitle(currentDestination, features)) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) { innerPadding ->
        NavigationHost(navController, innerPadding, features)
    }
}

fun getToolbarTitle(
    currentDestination: NavDestination?, features: List<Pair<String, String>>
): String {
    return features.find {
        currentDestination?.route?.startsWith(it.second.take(10)) == true
    }?.first ?: "Google Map Demo"
}
