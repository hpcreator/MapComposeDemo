package com.hpcreation.mapComposeDemo.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.R

@Composable
fun MapStylingScreen(paddingValues: PaddingValues) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val stylingOptions = listOf("JSON Style", "Cloud Style")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Styling Option Selection
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            stylingOptions.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index, count = stylingOptions.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(text = option) })
            }
        }
        when (selectedIndex) {
            0 -> DemoJsonStyle(LocalContext.current)
            1 -> DemoCloudStyle(LocalContext.current)
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DemoJsonStyle(context: Context) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            com.google.android.gms.maps.model.LatLng(23.0, 80.0), 5.0f
        ) // Center over India
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            Log.d("MapStyling", "Map has finished loading")
        }) {
        MapEffect(Unit) { googleMap ->
            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
                )
                if (!success) {
                    Log.e("MapStyling", "Failed to apply JSON style.")
                }
            } catch (e: Exception) {
                Log.e("MapStyling", "Error loading JSON style: ${e.message}")
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DemoCloudStyle(context: Context) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            com.google.android.gms.maps.model.LatLng(23.0, 80.0), 5.0f
        ) // Center over India
    }

    val cloudStyleId = context.getString(R.string.cloudStyleId) // Replace with your actual style ID

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            Log.d("MapStyling", "Map has finished loading")
        }) {
        MapEffect(Unit) { googleMap ->
            try {
                googleMap.setMapStyle(
                    MapStyleOptions("maps://styles/$cloudStyleId")
                )
            } catch (e: Exception) {
                Log.e("MapStyling", "Error applying cloud style: ${e.message}")
            }
        }
    }
}