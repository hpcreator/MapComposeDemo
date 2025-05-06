package com.hpcreation.mapComposeDemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.model.PlacePrediction
import com.hpcreation.mapComposeDemo.data.repo.MapRepository
import com.hpcreation.mapComposeDemo.utils.ApiKeyProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdvanceDataViewmodel(
    val markerRepository: MapRepository, private val apiKeyProvider: ApiKeyProvider
) : ViewModel() {
    private val _predictions = MutableStateFlow<List<PlacePrediction>>(emptyList())
    val predictions: StateFlow<List<PlacePrediction>> = _predictions

    private val _selectedPlace = MutableStateFlow<LatLng?>(null)
    val selectedPlace: StateFlow<LatLng?> = _selectedPlace

    // Fetch place predictions based on user query
    fun fetchPredictions(query: String) {
        viewModelScope.launch {
            val result =
                markerRepository.fetchPredictions(query, apiKeyProvider.getGoogleMapsApiKey())
            _predictions.value = result
        }
    }

    // Fetch place details for selected prediction
    fun fetchPlaceDetails(placeId: String) {
        viewModelScope.launch {
            val result =
                markerRepository.fetchPlaceDetails(placeId, apiKeyProvider.getGoogleMapsApiKey())
            _selectedPlace.value = result
        }
    }

    private val _geocodingResult = MutableStateFlow<Pair<Double, Double>?>(null)
    val geocodingResult: StateFlow<Pair<Double, Double>?> = _geocodingResult

    private val _reverseGeocodingResult = MutableStateFlow<String?>(null)
    val reverseGeocodingResult: StateFlow<String?> = _reverseGeocodingResult

    // Perform geocoding
    fun geocodeAddress(address: String) {
        viewModelScope.launch {
            val result =
                markerRepository.geocodeAddress(address, apiKeyProvider.getGoogleMapsApiKey())
            _geocodingResult.value = result
        }
    }

    // Perform reverse geocoding
    fun reverseGeocode(lat: Double, lng: Double) {
        viewModelScope.launch {
            val result =
                markerRepository.reverseGeocode(lat, lng, apiKeyProvider.getGoogleMapsApiKey())
            _reverseGeocodingResult.value = result
        }
    }

    private val _markers = MutableStateFlow<List<MarkerData>>(emptyList())
    val markers: StateFlow<List<MarkerData>> = _markers.asStateFlow()

    init {
        // Fetch markers from repository
        _markers.value = markerRepository.getClusterMarkers()
    }
}