package com.hpcreation.mapComposeDemo.utils

import android.content.Context
import com.hpcreation.mapComposeDemo.R

class ApiKeyProvider(
    private val context: Context
) {
    fun getGoogleMapsApiKey(): String {
        return context.getString(R.string.mapKey)
    }
}