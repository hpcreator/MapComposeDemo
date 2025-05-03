package com.hpcreation.mapComposeDemo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.data.model.TravelModes.Companion.allModes
import com.hpcreation.mapComposeDemo.viewmodel.DirectionViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TravelModeScreen(paddingValues: PaddingValues) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.0, 80.0), 5.0f) // Center over India
    }

    val viewModel: DirectionViewmodel = koinViewModel()
    val routePoints by viewModel.routePoints.collectAsState()
    val travelModes = allModes

    LaunchedEffect(selectedIndex) {
        val selectedTravelMode = travelModes[selectedIndex].mode
        Log.d("TAG", "Selected travel mode: $selectedTravelMode")
        viewModel.fetchDirections(travelMode = selectedTravelMode)
    }

    LaunchedEffect(routePoints) {
        if (routePoints.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()
            routePoints.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 500), durationMs = 1000
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            travelModes.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index, count = travelModes.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(text = label.mode.replaceFirstChar { it.uppercase() }) })
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            // Draw route polyline
            if (routePoints.isNotEmpty()) {
                Polyline(
                    points = routePoints,
                    color = Color(0xFF0D47A1), // Deep blue like Google Maps
                    width = 25f, // Thicker
                    jointType = JointType.ROUND,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                    geodesic = true
                )

                // Origin Marker
                Marker(
                    state = MarkerState(position = routePoints.first()), title = "Start"
                )

                // Destination Marker
                Marker(
                    state = MarkerState(position = routePoints.last()), title = "Destination"
                )
            }
        }
    }
}