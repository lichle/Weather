package com.lichle.weather

import android.app.Application
import android.os.StrictMode
import com.lichle.weather.common.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.setDebugMode(BuildConfig.DEBUG)
    }

}