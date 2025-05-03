package com.hpcreation.mapComposeDemo.data.model

sealed class TravelModes(val mode: String) {
    object DRIVING : TravelModes("driving")
    object WALKING : TravelModes("walking")
    object TRANSIT : TravelModes("transit")

    companion object {
        val allModes = listOf(DRIVING, WALKING, TRANSIT)
    }
}