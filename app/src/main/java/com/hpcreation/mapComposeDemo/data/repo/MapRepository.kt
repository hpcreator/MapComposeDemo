package com.hpcreation.mapComposeDemo.data.repo

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileProvider
import com.hpcreation.mapComposeDemo.data.model.DirectionData
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.model.PlacePrediction

interface MapRepository {
    // Part1Demo Markers
    fun getDefaultMarkers(): List<MarkerData>

    //Markers for PolyLine, PolyGon and Circle shape
    fun getPolylineCoordinates(): List<LatLng>
    fun getPolygonCoordinates(): List<LatLng>
    fun getCircleCenter(): LatLng

    // Markers for Showing Direction between points
    fun getDefaultDirections(): DirectionData

    // Locations and ids for Advance data handle such as place, GeoCoding, etc.
    suspend fun fetchPredictions(query: String, apiKey: String): List<PlacePrediction>
    suspend fun fetchPlaceDetails(placeId: String, apiKey: String): LatLng
    suspend fun geocodeAddress(address: String, apiKey: String): Pair<Double, Double>?
    suspend fun reverseGeocode(lat: Double, lng: Double, apiKey: String): String?
    fun getClusterMarkers(): List<MarkerData>

    // Provide support for Offline Tiles
    fun getOfflineTileProvider(): TileProvider
    suspend fun downloadVisibleTiles(
        minX: Int, maxX: Int, minY: Int, maxY: Int, zoom: Int
    )
}