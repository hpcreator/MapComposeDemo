package com.hpcreation.mapComposeDemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.compose.MapType
import com.hpcreation.mapComposeDemo.ui.screens.MapScreen
import com.hpcreation.mapComposeDemo.ui.theme.MapComposeDemoTheme
import com.hpcreation.mapComposeDemo.viewmodel.MapViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val mapViewModel: MapViewModel = koinViewModel()

            MapComposeDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ), title = {
                                Text("Google Map Demo")
                            })
                    }) { innerPadding ->
                    MapScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        markers = mapViewModel.markers.map { marker ->
                            if (marker.title!!.contains("IIM")) {
                                marker.copy(iconColor = MaterialTheme.colorScheme.primary)
                            } else {
                                marker
                            }
                        },
                        mapType = MapType.NORMAL,
                        zoomControlsEnabled = true,
                        gestureEnabled = true,
                        onMapReady = {
                            Toast.makeText(context, "Map is ready", Toast.LENGTH_LONG).show()
                        },
                        onMapLongPressed = { latLng ->
                            mapViewModel.onMapLongPressed(latLng, context)
                        },
                        dynamicMarker = mapViewModel.selectedMarker
                    )
                }
            }
        }
    }
}