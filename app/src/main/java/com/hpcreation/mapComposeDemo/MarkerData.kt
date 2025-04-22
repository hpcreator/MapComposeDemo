package com.hpcreation.mapComposeDemo

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class MarkerData(
    val position: LatLng,
    val title: String? = "",
    val snippet: String? = "",
    val iconColor: Color? = null,
    @DrawableRes val customIcon: Int? = null
)