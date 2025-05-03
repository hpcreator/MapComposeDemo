package com.hpcreation.mapComposeDemo.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.repo.MarkerRepository
import com.hpcreation.mapComposeDemo.ui.extensions.fetchAddress

class MapViewModel(markerRepository: MarkerRepository) : ViewModel() {
    val markers = mutableStateListOf<MarkerData>()
    var markerList = mutableStateListOf<MarkerData?>(null)
    private var lastAddTime = 0L
    private val debounceThresholdMs = 1000L // 1 second

    init {
        // Load default markers
        markers.addAll(markerRepository.getDefaultMarkers())
    }

    suspend fun onMapLongPressed(latLng: LatLng, context: Context) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAddTime < debounceThresholdMs) return

        lastAddTime = currentTime

        markerList.add(
            MarkerData(
                position = latLng,
                title = "Selected Address",
                snippet = context.fetchAddress(latLng)
            )
        )
    }
}