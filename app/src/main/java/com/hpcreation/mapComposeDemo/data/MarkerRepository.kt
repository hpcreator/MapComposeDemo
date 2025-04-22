package com.hpcreation.mapComposeDemo.data

import com.hpcreation.mapComposeDemo.MarkerData

interface MarkerRepository {
    fun getDefaultMarkers(): List<MarkerData>
}