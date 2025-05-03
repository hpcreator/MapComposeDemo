package com.hpcreation.mapComposeDemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navController: NavController, paddingValues: PaddingValues, features: List<Pair<String, String>>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(features) { (title, route) ->
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .padding(vertical = 5.dp)
                    .aspectRatio(1.8f),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    navController.navigate(route = route)

                }) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = title,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        rememberNavController(), PaddingValues(20.dp), listOf(
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route"),
            Pair("Title", "Route")
        )
    )
}