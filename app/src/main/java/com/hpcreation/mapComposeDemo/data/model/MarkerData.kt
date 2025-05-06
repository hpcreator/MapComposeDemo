package com.hpcreation.mapComposeDemo.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng

/**
 * Data class representing a marker on the map.
 *
 * @param position The geographical position of the marker.
 * @param title The title displayed on the marker.
 * @param snippet Additional information displayed on the marker.
 * @param iconColor The color of the marker icon.
 * @param customIcon A custom icon resource for the marker.
 */
data class MarkerData(
    val position: LatLng,
    val title: String? = "",
    val snippet: String? = "",
    val iconColor: Color? = null,
    @DrawableRes val customIcon: Int? = null
)