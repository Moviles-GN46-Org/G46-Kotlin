package com.example.g46_kotlin

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Configuration.getInstance().load(this, applicationContext.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    }
}
