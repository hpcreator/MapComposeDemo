package com.hpcreation.mapComposeDemo

import android.app.Application
import com.hpcreation.mapComposeDemo.di.appModule
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule) }
    }
}