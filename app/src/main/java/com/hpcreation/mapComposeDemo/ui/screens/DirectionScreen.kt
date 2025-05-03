package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.hpcreation.mapComposeDemo.viewmodel.DirectionViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DirectionScreen(
    paddingValues: PaddingValues
) {
    val viewModel: DirectionViewmodel = koinViewModel()
    val routePoints by viewModel.routePoints.collectAsState()
    val distanceText by viewModel.distanceText.collectAsState()
    val durationText by viewModel.durationText.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.0, 80.0),5.0f) // Center over India
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

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(), cameraPositionState = cameraPositionState
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
        AnimatedVisibility(
            visible = true, // Always show
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(paddingValues = paddingValues)
                .padding(vertical = 5.dp) // 👈 Leaves space below the app bar
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.8f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Distance: $distanceText",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duration: $durationText",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}