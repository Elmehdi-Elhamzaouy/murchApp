package com.example.login_system_test01

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class LightMode : Application() {
    override fun onCreate() {
        super.onCreate()

        // Always use light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}