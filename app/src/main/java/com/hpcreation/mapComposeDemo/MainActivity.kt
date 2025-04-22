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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapType
import com.hpcreation.mapComposeDemo.ui.extensions.fetchAddress
import com.hpcreation.mapComposeDemo.ui.screens.MapScreen
import com.hpcreation.mapComposeDemo.ui.theme.MapComposeDemoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val selectedPosition = remember { mutableStateOf<LatLng?>(null) }
            val address = remember { mutableStateOf<String?>(null) }

            val markers = remember { mutableStateListOf<MarkerData>() }

            markers.add(
                MarkerData(
                    position = LatLng(23.022313639895916, 72.53758245374887),
                    title = "Valtech, Ahmedabad",
                    snippet = "This is Valtech office at Ahmedabad, Gujarat",
                    iconColor = null,
                    customIcon = R.drawable.valtech_logo
                )
            )
            markers.add(
                MarkerData(
                    position = LatLng(23.031806823932076, 72.53452032615851),
                    title = "IIM, Ahmedabad",
                    snippet = "This is IIM College of Ahmedabad, Gujarat",
                    iconColor = MaterialTheme.colorScheme.primaryContainer,
                    customIcon = null
                )
            )
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
                        markers = markers,
                        mapType = MapType.NORMAL,
                        zoomControlsEnabled = true,
                        gestureEnabled = true,
                        onMapReady = {
                            Toast.makeText(context, "Map is ready", Toast.LENGTH_LONG).show()
                        },
                        onMapLongPressed = { latLng ->
                            selectedPosition.value = latLng
                            context.fetchAddress(latLng) {
                                address.value = it
                            }
                        },
                        dynamicMarker = selectedPosition.value?.let {
                            MarkerData(
                                position = it, title = "Selected Address", snippet = address.value
                            )
                        })
                }
            }
        }
    }
}