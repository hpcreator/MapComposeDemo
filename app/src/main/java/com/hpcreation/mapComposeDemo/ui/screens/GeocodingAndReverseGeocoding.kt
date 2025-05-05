package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hpcreation.mapComposeDemo.viewmodel.AdvanceDataViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeocodingAndReverseGeocoding() {
    val viewModel: AdvanceDataViewmodel = koinViewModel()
    val geocodingResult by viewModel.geocodingResult.collectAsState()
    val reverseGeocodingResult by viewModel.reverseGeocodingResult.collectAsState()

    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Geocoding Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            Text("Geocoding", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Enter Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.geocodeAddress(address) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Geocode")
            }

            Spacer(modifier = Modifier.height(8.dp))

            geocodingResult?.let { result ->
                Text("Lat: ${result.first}, Lng: ${result.second}")
            }
        }

        HorizontalDivider(thickness = 1.dp)

        // Reverse Geocoding Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            Text("Reverse Geocoding", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.reverseGeocode(
                        latitude.toDoubleOrNull() ?: 0.0, longitude.toDoubleOrNull() ?: 0.0
                    )
                }, modifier = Modifier.align(Alignment.End)
            ) {
                Text("Reverse Geocode")
            }

            Spacer(modifier = Modifier.height(8.dp))

            reverseGeocodingResult?.let { result ->
                Text("Address: $result")
            }
        }
    }
}