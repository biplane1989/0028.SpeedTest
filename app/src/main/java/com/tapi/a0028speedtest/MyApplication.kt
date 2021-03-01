package com.tapi.a0028speedtest

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.tapi.a0028speedtest.database.SpeedTestDatabase
import com.tapi.a0028speedtest.util.PreferencesHelper

class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        PreferencesHelper.start(appContext)
        SpeedTestDatabase.create(appContext)
    }

}