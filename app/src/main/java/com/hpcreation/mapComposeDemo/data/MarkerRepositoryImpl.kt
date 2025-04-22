package com.hpcreation.mapComposeDemo.data

import com.google.android.gms.maps.model.LatLng
import com.hpcreation.mapComposeDemo.MarkerData
import com.hpcreation.mapComposeDemo.R

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
}