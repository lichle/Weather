package com.lichle.weather.common

import android.util.Log

object Logger {

    @Volatile
    private var _enable = false

    fun setDebugMode(isDebug: Boolean) {
        _enable = isDebug
    }

    fun v(tag: String, msg: String) {
        if (_enable){
            Log.v(tag, msg)
        }
    }

    fun v(tag: String, msg: String, tr: Throwable?) {
        if (_enable){
            Log.v(tag, msg)
            tr?.printStackTrace()
        }
    }

    fun d(tag: String, msg: String) {
        if (_enable){
            Log.d(tag, msg)
        }
    }

    fun d(tag: String, msg: String, tr: Throwable?) {
        if (_enable){
            Log.d(tag, msg)
            tr?.printStackTrace()
        }
    }

    fun i(tag: String, msg: String) {
        if (_enable){
            Log.i(tag, msg)
            println("INFO: $tag: $msg")
        }
    }

    fun i(tag: String, msg: String, tr: Throwable?) {
        if (_enable){
            Log.i(tag, msg)
            tr?.printStackTrace()
        }
    }

    fun w(tag: String, msg: String) {
        if (_enable){
            Log.w(tag, msg)
            println("WARN: $tag: $msg")
        }
    }

    fun w(tag: String, msg: String, tr: Throwable?) {
        if (_enable){
            Log.w(tag, msg)
            tr?.printStackTrace()
        }
    }

    fun e(tag: String, msg: String) {
        if (_enable){
            Log.e(tag, msg)
        }
    }

    fun e(tag: String, msg: String, tr: Throwable?) {
        if (_enable){
            Log.e(tag, msg)
            tr?.printStackTrace()
        }
    }

}