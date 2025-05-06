package com.hpcreation.mapComposeDemo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.viewmodel.OfflineMapViewmodel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.tan


@Composable
fun OfflineMapsScreen(paddingValues: PaddingValues) {
    val viewModel: OfflineMapViewmodel = koinViewModel()
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.0, 80.0), 5.0f) // Center over India
    }

    val isOffline = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            if (isOffline.value) {
                TileOverlay(
                    tileProvider = viewModel.offlineTileProvider
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                // Download visible tiles
                val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                if (bounds != null) {
                    val zoom = cameraPositionState.position.zoom.toInt()
                    val minXY = latLngToTileXY(bounds.southwest, zoom)
                    val maxXY = latLngToTileXY(bounds.northeast, zoom)

                    viewModel.downloadTiles(
                        minXY.first, maxXY.first, minXY.second, maxXY.second, zoom
                    )
                    Toast.makeText(context, "Downloading tiles...", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Download Visible Map")
            }

            Button(onClick = {
                isOffline.value = !isOffline.value
            }) {
                Text(if (isOffline.value) "Switch to Online Map" else "Switch to Offline Map")
            }
        }
    }
}

fun latLngToTileXY(latLng: LatLng, zoom: Int): Pair<Int, Int> {
    val n = 1 shl zoom
    val x = ((latLng.longitude + 180.0) / 360.0 * n).toInt()
    val latRad = Math.toRadians(latLng.latitude)
    val y = ((1.0 - ln(tan(latRad) + 1 / cos(latRad)) / Math.PI) / 2.0 * n).toInt()
    return Pair(x, y)
}