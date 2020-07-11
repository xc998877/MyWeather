package com.myweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyWeatherApplication:Application() {
    companion object{
        const val TOKEN="Lat5Y0YsKPh4tkAW"
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}