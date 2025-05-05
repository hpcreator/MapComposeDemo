package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.viewmodel.AdvanceDataViewmodel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.floor

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ClusteringScreen() {
    val viewModel: AdvanceDataViewmodel = koinViewModel()
    val markers by viewModel.markers.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        if (markers.isNotEmpty()) {
            // Move camera to the first marker
            position = CameraPosition.fromLatLngZoom(
                markers.first().position, 10f // Default zoom level
            )
        }
    }

    val zoom = cameraPositionState.position.zoom

    // Cluster markers dynamically based on zoom
    val clusteredMarkers = remember(zoom, markers) {
        clusterMarkers(markers, zoom)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
    ) {
        clusteredMarkers.forEach { cluster ->
            Marker(
                state = MarkerState(position = cluster.position), title = cluster.title
            )
        }
    }
    LaunchedEffect(markers) {
        if (markers.isNotEmpty()) {
            moveCameraToFitMarkers(markers, cameraPositionState)
        }
    }
}

fun moveCameraToFitMarkers(markers: List<MarkerData>, cameraPositionState: CameraPositionState) {
    val boundsBuilder = LatLngBounds.builder()
    markers.forEach { marker ->
        boundsBuilder.include(marker.position)
    }

    val bounds = boundsBuilder.build()
    cameraPositionState.move(
        CameraUpdateFactory.newLatLngBounds(bounds, 100) // 100 padding around the bounds
    )
}

fun clusterMarkers(items: List<MarkerData>, zoom: Float): List<MarkerData> {
    val gridSize = when {
        zoom > 15 -> 0.005  // More precise at higher zoom
        zoom > 13 -> 0.02
        zoom > 10 -> 0.05
        else -> 0.1  // Heavy clustering at lower zoom
    }

    val clusters = mutableMapOf<Pair<Int, Int>, MutableList<MarkerData>>()

    items.forEach { item ->
        val key = Pair(
            floor(item.position.latitude / gridSize).toInt(),
            floor(item.position.longitude / gridSize).toInt()
        )
        clusters.getOrPut(key) { mutableListOf() }.add(item)
    }

    return clusters.values.map { groupedItems ->
        if (groupedItems.size == 1) {
            // Single marker, no clustering
            groupedItems[0]
        } else {
            // Create cluster marker
            val avgLat = groupedItems.map { it.position.latitude }.average()
            val avgLng = groupedItems.map { it.position.longitude }.average()
            MarkerData(
                LatLng(avgLat, avgLng), "${groupedItems.size} markers"
            )
        }
    }
}