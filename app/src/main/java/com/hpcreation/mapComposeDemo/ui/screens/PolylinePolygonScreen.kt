package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.viewmodel.PolylineViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PolylinePolygonScreen() {

    val viewModel: PolylineViewmodel = koinViewModel()

    val polyline = viewModel.polylineCoordinates
    val polygon = viewModel.polygonCoordinates
    val circleCenter = viewModel.circleCenter

    val defaultCameraPosition = remember {
        CameraPosition.fromLatLngZoom(LatLng(21.0, 78.0), 5.0f)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        // Draw Polyline
        Polyline(
            points = polyline, color = Color.Blue, width = 10f, geodesic = true
        )

        // Draw Polygon
        Polygon(
            points = polygon,
            strokeColor = Color.Red,
            strokeWidth = 6f,
            fillColor = Color(0x33FF0000)
        )
        Circle(
            center = circleCenter,
            fillColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            strokeColor = MaterialTheme.colorScheme.error,
            radius = 250000.0
        )
    }
}