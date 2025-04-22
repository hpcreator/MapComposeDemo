package com.hpcreation.mapComposeDemo.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.MarkerData
import com.hpcreation.mapComposeDemo.data.MarkerRepository
import com.hpcreation.mapComposeDemo.ui.extensions.fetchAddress

class MapViewModel(markerRepository: MarkerRepository) : ViewModel() {
    val markers = mutableStateListOf<MarkerData>()
    val selectedPosition = mutableStateOf<LatLng?>(null)
    val address = mutableStateOf<String?>(null)

    val selectedMarker: MarkerData?
        get() = selectedPosition.value?.let {
            MarkerData(
                position = it, title = "Selected Address", snippet = address.value
            )
        }

    init {
        // Load default markers
        markers.addAll(markerRepository.getDefaultMarkers())
    }

    fun onMapLongPressed(latLng: LatLng, context: Context) {
        selectedPosition.value = latLng
        context.fetchAddress(latLng) {
            address.value = it
        }
    }
}