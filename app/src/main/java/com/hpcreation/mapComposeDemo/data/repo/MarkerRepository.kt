package com.hpcreation.mapComposeDemo.data.repo

import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.model.DirectionData
import com.hpcreation.mapComposeDemo.data.model.MarkerData

interface MarkerRepository {
    fun getDefaultMarkers(): List<MarkerData>
    fun getPolylineCoordinates(): List<LatLng>
    fun getPolygonCoordinates(): List<LatLng>
    fun getCircleCenter(): LatLng

    fun getDefaultDirections(): DirectionData
}