package com.hpcreation.mapComposeDemo.ui.extensions

import android.content.Context
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
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

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Context.fetchAddress(latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(this@fetchAddress, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(
                        latLng.latitude, latLng.longitude, 1, object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                val address =
                                    addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
                                continuation.resume(address, null)
                            }

                            override fun onError(errorMessage: String?) {
                                continuation.resume("Unable to fetch address", null)
                            }
                        })
                }
            } else {
                // Legacy fallback
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unable to fetch address"
        }
    }
}