package com.hpcreation.mapComposeDemo.data.repo

import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.model.ClusterMarkerItem
import com.hpcreation.mapComposeDemo.data.model.DirectionData
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.model.PlacePrediction

interface MarkerRepository {
    fun getDefaultMarkers(): List<MarkerData>
    fun getPolylineCoordinates(): List<LatLng>
    fun getPolygonCoordinates(): List<LatLng>
    fun getCircleCenter(): LatLng

    fun getDefaultDirections(): DirectionData

    suspend fun fetchPredictions(query: String, apiKey: String): List<PlacePrediction>
    suspend fun fetchPlaceDetails(placeId: String, apiKey: String): LatLng
    suspend fun geocodeAddress(address: String, apiKey: String): Pair<Double, Double>?
    suspend fun reverseGeocode(lat: Double, lng: Double, apiKey: String): String?
    fun getClusterMarkers(): List<MarkerData>
}