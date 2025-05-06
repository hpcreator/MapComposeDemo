package com.hpcreation.mapComposeDemo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.repo.MapRepository

class PolylineViewmodel(markerRepository: MapRepository) : ViewModel() {
    val polylineCoordinates = mutableStateListOf<LatLng>()
    val polygonCoordinates = mutableStateListOf<LatLng>()
    var circleCenter by mutableStateOf(LatLng(0.0, 0.0))
        private set

    init {
        polylineCoordinates.addAll(markerRepository.getPolylineCoordinates())
        polygonCoordinates.addAll(markerRepository.getPolygonCoordinates())
        circleCenter = markerRepository.getCircleCenter()
    }
}