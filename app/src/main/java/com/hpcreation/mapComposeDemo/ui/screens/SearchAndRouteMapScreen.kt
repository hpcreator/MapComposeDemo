package com.hpcreation.mapComposeDemo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndRouteMapScreen(
    modifier: Modifier = Modifier, onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    var startLocation by remember { mutableStateOf<LatLng?>(null) }
    var endLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Location") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                // Simulate search logic here
                coroutineScope.launch {
                    Toast.makeText(context, "Searching for $searchQuery...", Toast.LENGTH_SHORT)
                        .show()
                }
            }, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text("Search")
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                if (startLocation == null) {
                    startLocation = latLng
                    Toast.makeText(context, "Start Location Set", Toast.LENGTH_SHORT).show()
                } else {
                    endLocation = latLng
                    Toast.makeText(context, "End Location Set", Toast.LENGTH_SHORT).show()
                }
            }) {
            startLocation?.let {
                Marker(state = rememberMarkerState(position = it), title = "Start Location")
            }
            endLocation?.let {
                Marker(state = rememberMarkerState(position = it), title = "End Location")
            }
        }
        if (startLocation != null && endLocation != null) {
            Button(
                onClick = {
                    // Simulate route calculation here
                    coroutineScope.launch {
                        Toast.makeText(context, "Calculating route...", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text("Show Route")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SearchAndRouteMapScreenPreview() {
    SearchAndRouteMapScreen(modifier = Modifier) {}
}