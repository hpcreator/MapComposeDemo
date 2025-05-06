package com.hpcreation.mapComposeDemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.data.model.TravelModes
import com.hpcreation.mapComposeDemo.data.repo.MapRepository
import com.hpcreation.mapComposeDemo.utils.ApiKeyProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class DirectionViewmodel(
    private val repository: MapRepository, private val apiKeyProvider: ApiKeyProvider
) : ViewModel() {
    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints.asStateFlow()

    private val _distanceText = MutableStateFlow("")
    val distanceText: StateFlow<String> = _distanceText.asStateFlow()
    private val _durationText = MutableStateFlow("")
    val durationText: StateFlow<String> = _durationText.asStateFlow()

    fun fetchDirections(travelMode: String = TravelModes.DRIVING.mode) {
        val direction = repository.getDefaultDirections()
        val origin = "${direction.origin.latitude},${direction.origin.longitude}"
        val destination = "${direction.destination.latitude},${direction.destination.longitude}"

        viewModelScope.launch {
            try {
                val url =
                    "https://maps.googleapis.com/maps/api/directions/json?" + "origin=$origin&destination=$destination&key=${apiKeyProvider.getGoogleMapsApiKey()}&mode=$travelMode"

                val response = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }

                val jsonObject = JSONObject(response)
                val routes = jsonObject.getJSONArray("routes")
                if (routes.length() > 0) {
                    val route = routes.getJSONObject(0)
                    val overviewPolyline = route.getJSONObject("overview_polyline")
                    val points = overviewPolyline.getString("points")
                    val decodedPath = decodePolyline(points)
                    _routePoints.value = decodedPath

                    val legs = route.getJSONArray("legs")
                    if (legs.length() > 0) {
                        val leg = legs.getJSONObject(0)
                        val distance = leg.getJSONObject("distance").getString("text")
                        val duration = leg.getJSONObject("duration").getString("text")
                        _distanceText.value = distance
                        _durationText.value = duration
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("TAG", "fetchDirections: ${e.message}")
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5, lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }
}