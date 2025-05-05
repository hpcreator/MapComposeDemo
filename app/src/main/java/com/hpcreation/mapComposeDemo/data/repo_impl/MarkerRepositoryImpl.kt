package com.hpcreation.mapComposeDemo.data.repo_impl

import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.R
import com.hpcreation.mapComposeDemo.data.model.DirectionData
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.model.PlacePrediction
import com.hpcreation.mapComposeDemo.data.repo.MarkerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MarkerRepositoryImpl : MarkerRepository {
    override fun getDefaultMarkers(): List<MarkerData> {
        return listOf(
            MarkerData(
                position = LatLng(23.022313639895916, 72.53758245374887),
                title = "Valtech, Ahmedabad",
                snippet = "This is Valtech office at Ahmedabad, Gujarat",
                customIcon = R.drawable.valtech_logo
            ), MarkerData(
                position = LatLng(23.031806823932076, 72.53452032615851),
                title = "IIM, Ahmedabad",
                snippet = "This is IIM College of Ahmedabad, Gujarat",
            )
        )
    }

    override fun getPolylineCoordinates(): List<LatLng> {
        return listOf(
            LatLng(21.1458, 79.0882), // Nagpur, Maharashtra
            LatLng(21.6680, 78.1120), // Chhindwara, MP (slight curve NE)
            LatLng(22.0574, 77.7523), // Betul, MP (further NW)
            LatLng(21.8232, 76.3526), // Khandwa, MP (curve west)
            LatLng(21.2514, 75.7139), // Jalgaon, Maharashtra (curve SW)
            LatLng(20.5937, 78.9629)
        )
    }

    override fun getPolygonCoordinates(): List<LatLng> {
        return listOf(
            LatLng(13.5, 77.0),   // Top-left (near Bengaluru/Andhra border)
            LatLng(13.5, 81.5),   // Top-right (coast of Andhra)
            LatLng(9.5, 81.5),    // Bottom-right (south TN coast)
            LatLng(9.5, 77.0)
        )
    }

    override fun getCircleCenter(): LatLng = LatLng(23.0225, 72.5714) // Ahmedabad

    override fun getDefaultDirections() = DirectionData(
        origin = LatLng(22.692641162527536, 72.86318841852814),         // Nadiad, Gujarat
        destination = LatLng(
            23.022244519693295, 72.53759318258524
        )     // Valtech, Ahmedabad, Gujarat
    )

    override suspend fun fetchPredictions(query: String, apiKey: String): List<PlacePrediction> {
        return withContext(Dispatchers.IO) {
            val url =
                URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$query&key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val predictions = mutableListOf<PlacePrediction>()
                    val predictionsArray = jsonResponse.optJSONArray("predictions")
                    for (i in 0 until (predictionsArray?.length() ?: 0)) {
                        val prediction = predictionsArray?.getJSONObject(i)
                        predictions.add(
                            PlacePrediction(
                                placeId = prediction?.getString("place_id").orEmpty(),
                                description = prediction?.getString("description").orEmpty()
                            )
                        )
                    }
                    predictions
                } else {
                    emptyList() // Return an empty list if the response is not OK
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    override suspend fun fetchPlaceDetails(placeId: String, apiKey: String): LatLng {
        return withContext(Dispatchers.IO) {
            val url =
                URL("https://maps.googleapis.com/maps/api/place/details/json?place_id=$placeId&key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val location = jsonResponse.optJSONObject("result")?.optJSONObject("geometry")
                        ?.optJSONObject("location")

                    LatLng(
                        location?.getDouble("lat") ?: 0.0, location?.getDouble("lng") ?: 0.0
                    )
                } else {
                    LatLng(0.0, 0.0) // Return a default LatLng if the response is not OK
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    override suspend fun geocodeAddress(address: String, apiKey: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            val url = URL(
                "https://maps.googleapis.com/maps/api/geocode/json?address=${
                    address.replace(
                        " ", "+"
                    )
                }&key=$apiKey"
            )
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val location = jsonResponse.optJSONArray("results")?.optJSONObject(0)
                        ?.optJSONObject("geometry")?.optJSONObject("location")

                    location?.let {
                        Pair(it.getDouble("lat"), it.getDouble("lng"))
                    }
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    override suspend fun reverseGeocode(lat: Double, lng: Double, apiKey: String): String? {
        return withContext(Dispatchers.IO) {
            val url =
                URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    jsonResponse.optJSONArray("results")?.optJSONObject(0)
                        ?.getString("formatted_address")
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    override fun getClusterMarkers(): List<MarkerData> {
        return listOf(
            MarkerData(
                LatLng(23.047018111785995, 72.51531655188046),
                "Gurdwara Gobind Dham, Thaltej"
            ),
            MarkerData(
                LatLng(23.048997533900216, 72.51576716299824),
                "Mercedes-Benz Landmark Cars"
            ),
            MarkerData(LatLng(23.04381928992368, 72.5171485001726), "Gulmohar"),
            MarkerData(LatLng(23.042710692072216, 72.51462391784206), "The Grand Bhagwati"),
        )
    }
}