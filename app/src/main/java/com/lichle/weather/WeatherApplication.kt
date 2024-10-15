package com.lichle.weather

import android.app.Application
import android.os.StrictMode
import com.lichle.weather.common.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Set debug mode in Logger based on app's BuildConfig
        Logger.setDebugMode(BuildConfig.DEBUG)

        if (BuildConfig.DEBUG) {
            setupStrictMode()
        }
    }

    private fun setupStrictMode() {
        // Thread policy for detecting UI thread violations
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyDialog()
                .penaltyFlashScreen()
                .build()
        )

        // VM policy for detecting memory leaks and resource issues
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .penaltyLog()
                .build()
        )
    }

}