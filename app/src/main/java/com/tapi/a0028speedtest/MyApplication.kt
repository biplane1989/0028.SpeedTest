package com.tapi.a0028speedtest

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.tapi.a0028speedtest.database.HistoryDatabase
import com.tapi.a0028speedtest.util.PreferencesHelper

class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        PreferencesHelper.start(appContext)
        HistoryDatabase.create(appContext)
    }

}