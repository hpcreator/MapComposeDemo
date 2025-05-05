package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hpcreation.mapComposeDemo.viewmodel.AdvanceDataViewmodel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    val viewModel: AdvanceDataViewmodel = koinViewModel()
    val predictions by viewModel.predictions.collectAsState()
    val selectedPlace by viewModel.selectedPlace.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(23.0, 80.0), 5.0f) // Default position over India
    }

    var query by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // AutoComplete TextField
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded, onExpandedChange = { isDropdownExpanded = it }) {
            OutlinedTextField(
                value = query, onValueChange = {
                    query = it
                    viewModel.fetchPredictions(it)
                    isDropdownExpanded = it.isNotEmpty()
                }, label = { Text("Search Places") }, modifier = Modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryEditable, enabled = true
                    )
                    .fillMaxWidth()
            )

            // Dropdown Menu
            ExposedDropdownMenu(
                expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                predictions.forEach { prediction ->
                    DropdownMenuItem(onClick = {
                        query = prediction.description // Update text field with selected value
                        isDropdownExpanded = false // Close dropdown
                        viewModel.fetchPlaceDetails(prediction.placeId) // Fetch place details
                    }, text = { Text(prediction.description) })
                }
            }
        }

        // Button to Show on Map
        Button(
            onClick = {
                selectedPlace?.let { place ->
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(place, 15f)
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Show on Map")
        }

        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            selectedPlace?.let { place ->
                Marker(
                    state = MarkerState(position = place), title = "Selected Place"
                )
            }
        }
    }
}