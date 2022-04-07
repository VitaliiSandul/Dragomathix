package com.sandul.mathtable

import android.app.Application

class PlayerSettingsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PlayerSettingsRepository.initialize(this)
    }
}