package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AdvancedDataHandlingScreen(paddingValues: PaddingValues) {
    var selectedFeatureIndex by remember { mutableIntStateOf(0) }

    val features = listOf("Places API", "Geocoding", "Clustering")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Feature Selection
        SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            features.forEachIndexed { index, feature ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index, count = features.size
                    ),
                    onClick = { selectedFeatureIndex = index },
                    selected = index == selectedFeatureIndex,
                    label = { Text(text = feature) })
            }
        }
        when (selectedFeatureIndex) {
            0 -> PlacesScreen()
            1 -> GeocodingAndReverseGeocoding()
            2 -> ClusteringScreen()
        }
    }
}