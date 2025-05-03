package com.hpcreation.mapComposeDemo

import com.google.maps.android.compose.MapType

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MapScreen : Screen("mapScreen") {
        fun routeWithArgs(
            mapType: MapType,
            zoomControlsEnabled: Boolean,
            gestureEnabled: Boolean
        ) = "map_screen/${mapType.name}/${zoomControlsEnabled}/${gestureEnabled}"

        const val ROUTE_PATTERN =
            "map_screen/{mapType}/{zoomControlsEnabled}/{gestureEnabled}"

    }

    object PolyLinePolyGon : Screen("polylinePolygon")
    object DirectionsDistance : Screen("directionsDistance")
    object TravelMode : Screen("travelMode")
    object MapStyling : Screen("mapStyling")
    object PlacesGeocoding : Screen("placesGeocoding")
    object Clustering : Screen("clustering")
    object Geofencing : Screen("geofencing")
    object OfflineMaps : Screen("offlineMaps")
    object Performance : Screen("performance")
    object SecurityAccessibility : Screen("securityAccessibility")
}