package com.hpcreation.mapComposeDemo.data.network

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class TileCacheManager(private val context: Context) {

    private val tileSize = 256

    fun getOfflineTileProvider(): TileProvider {
        return TileProvider { x, y, zoom ->
            val tileFile = File(context.cacheDir, "tiles/$zoom/$x/$y.png")
            if (tileFile.exists()) {
                Tile(tileSize, tileSize, tileFile.readBytes())
            } else {
                null
            }
        }
    }

    suspend fun downloadTilesForArea(
        minX: Int, maxX: Int, minY: Int, maxY: Int, zoom: Int
    ) {
        withContext(Dispatchers.IO) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    downloadTile(x, y, zoom)
                }
            }
        }
    }

    private fun downloadTile(x: Int, y: Int, zoom: Int) {
        try {
            val url = URL("https://tile.openstreetmap.org/$zoom/$x/$y.png")
            val conn = url.openConnection() as HttpURLConnection
            conn.connect()

            val dir = File(context.cacheDir, "tiles/$zoom/$x")
            dir.mkdirs()

            val tileFile = File(dir, "$y.png")
            val input: InputStream = conn.inputStream
            val output = FileOutputStream(tileFile)

            input.copyTo(output)
            output.close()
            input.close()

            Log.d("TileCache", "Downloaded tile: z$zoom x$x y$y")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}