package com.hpcreation.mapComposeDemo.data.model

import com.google.android.gms.maps.model.LatLng

data class DirectionData(
    val origin: LatLng,
    val destination: LatLng
)
