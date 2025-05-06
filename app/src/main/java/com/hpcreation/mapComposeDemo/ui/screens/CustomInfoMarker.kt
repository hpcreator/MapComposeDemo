package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberMarkerState
import com.hpcreation.mapComposeDemo.data.model.MarkerData
import com.hpcreation.mapComposeDemo.ui.extensions.bitmapDescriptorFromVector

@Composable
fun CustomInfoMarker(
    markerData: MarkerData
) {
    val context = LocalContext.current

    MarkerInfoWindow(
        state = rememberMarkerState(position = markerData.position),
        icon = context.bitmapDescriptorFromVector(markerData.customIcon!!, 150, 150),
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(35.dp, 35.dp, 35.dp, 35.dp)
                )
                .shadow(8.dp, RoundedCornerShape(35.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = markerData.title.orEmpty(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = markerData.snippet.orEmpty(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}