package com.hpcreation.mapComposeDemo.ui.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.hpcreation.mapComposeDemo.MarkerData
import com.hpcreation.mapComposeDemo.ui.extensions.argbToHue
import com.hpcreation.mapComposeDemo.ui.theme.MapComposeDemoTheme

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    markers: List<MarkerData>,
    mapType: MapType = MapType.NORMAL,
    zoomControlsEnabled: Boolean = true,
    gestureEnabled: Boolean = true,
    onMapReady: (() -> Unit)? = null,
    onMapLongPressed: ((LatLng) -> Unit)? = null,
    dynamicMarkers: List<MarkerData?>? = null
) {
    val defaultCameraPosition = remember {
        CameraPosition.fromLatLngZoom(
            markers.firstOrNull()?.position ?: LatLng(0.0, 0.0), 14f
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    val mapProperties = remember(mapType) {
        MapProperties(mapType = mapType)
    }
    val uiSettings = remember(zoomControlsEnabled, gestureEnabled) {
        MapUiSettings(
            zoomControlsEnabled = zoomControlsEnabled,
            scrollGesturesEnabled = gestureEnabled,
            zoomGesturesEnabled = gestureEnabled,
            tiltGesturesEnabled = gestureEnabled
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = {
            Log.e("TAG", "MapScreen: Map Loaded Successfully...")
            onMapReady?.invoke()
        },
        onMapLongClick = { latLng ->
            onMapLongPressed?.invoke(latLng)
        }) {
        markers.forEach { markerData ->
            if (markerData.customIcon != null) {
                CustomInfoMarker(markerData)
            } else {
                Marker(
                    state = rememberMarkerState(position = markerData.position),
                    title = markerData.title,
                    snippet = markerData.snippet,
                    icon = when {
                        markerData.iconColor != null -> markerData.iconColor.argbToHue()

                        else -> BitmapDescriptorFactory.defaultMarker()
                    }
                )
            }
        }
        dynamicMarkers?.forEach { marker ->
            marker?.let {
                Marker(
                    state = rememberMarkerState(
                        key = it.position.toString(),
                        position = it.position
                    ),
                    title = it.title,
                    snippet = it.snippet
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "LightPreview",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "DarkPreview",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MapScreenPreview() {
    val markers = listOf(
        MarkerData(
            position = LatLng(23.022313639895916, 72.53758245374887),
            title = "Valtech, Ahmedabad",
            snippet = "This is Valtech office at Ahmedabad, Gujarat",
            customIcon = null
        ), MarkerData(
            position = LatLng(23.031806823932076, 72.53452032615851),
            title = "IIM, Ahmedabad",
            snippet = "This is IIM College of Ahmedabad, Gujarat",
            iconColor = androidx.compose.ui.graphics.Color.Blue
        )
    )
    MapComposeDemoTheme {
        MapScreen(markers = markers, mapType = MapType.NORMAL)
    }
}
