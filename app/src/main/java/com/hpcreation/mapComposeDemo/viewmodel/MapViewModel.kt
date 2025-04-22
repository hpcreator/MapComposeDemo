package com.hpcreation.mapComposeDemo.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.MarkerData
import com.hpcreation.mapComposeDemo.data.MarkerRepository
import com.hpcreation.mapComposeDemo.ui.extensions.fetchAddress

class MapViewModel(markerRepository: MarkerRepository) : ViewModel() {
    val markers = mutableStateListOf<MarkerData>()
    var markerList = mutableStateListOf<MarkerData?>(null)

    init {
        // Load default markers
        markers.addAll(markerRepository.getDefaultMarkers())
    }

    suspend fun onMapLongPressed(latLng: LatLng, context: Context) {
        markerList.add(
            MarkerData(
                position = latLng,
                title = "Selected Address",
                snippet = context.fetchAddress(latLng)
            )
        )
    }
}