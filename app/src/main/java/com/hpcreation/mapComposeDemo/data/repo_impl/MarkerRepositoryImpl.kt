package com.hpcreation.mapComposeDemo.data.repo_impl

import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.R
import com.hpcreation.mapComposeDemo.data.model.DirectionData
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.data.repo.MarkerRepository

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
}