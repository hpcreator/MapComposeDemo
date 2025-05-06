package com.hpcreation.mapComposeDemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hpcreation.mapComposeDemo.data.repo.MapRepository
import kotlinx.coroutines.launch

class OfflineMapViewmodel(private val repository: MapRepository) : ViewModel() {

    val offlineTileProvider = repository.getOfflineTileProvider()

    fun downloadTiles(minX: Int, maxX: Int, minY: Int, maxY: Int, zoom: Int) {
        viewModelScope.launch {
            repository.downloadVisibleTiles(minX, maxX, minY, maxY, zoom)
        }
    }
}