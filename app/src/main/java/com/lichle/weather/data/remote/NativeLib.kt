package com.lichle.weather.data.remote

object NativeLib {
    init {
        System.loadLibrary("native-lib") // Load the native library
    }

    external fun getApiKey(): String // Declare the native method
}