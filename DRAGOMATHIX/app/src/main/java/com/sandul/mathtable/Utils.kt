package com.sandul.mathtable

import android.os.Handler
import android.os.Looper

object Utils {
    fun runOnUiThread(runnable: Runnable?) {
        val UIHandler = Handler(Looper.getMainLooper())
        UIHandler.post(runnable!!)
    }
}