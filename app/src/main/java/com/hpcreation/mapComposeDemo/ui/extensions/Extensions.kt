package com.hpcreation.mapComposeDemo.ui.extensions

import android.content.Context
import android.graphics.Canvas
import android.location.Geocoder
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import android.graphics.Color as androidColor

fun Color.argbToHue(): BitmapDescriptor {
    val hsv = FloatArray(3)
    androidColor.colorToHSV(this.toArgb(), hsv)
    return BitmapDescriptorFactory.defaultMarker(hsv[0]) // hue is at index 0
}

fun Context.bitmapDescriptorFromVector(
    @DrawableRes resId: Int, width: Int = 100, height: Int = 100
): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(this, resId)
        ?: throw IllegalArgumentException("Resource not found: $resId")

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Context.fetchAddress(latLng: LatLng, callback: (String) -> Unit) {
    val geocoder = Geocoder(this, Locale.getDefault())
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
            withContext(Dispatchers.Main) {
                callback(address)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                callback("Unable to fetch address")
            }
        }
    }
}