package com.tapi.a0028speedtest.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tapi.a0028speedtest.util.PreferencesHelper
import com.tapi.a0028speedtest.util.Utils
import java.util.*


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
    }
    protected open fun onPreCreate(): Boolean {
        return true
    }

    protected open fun onPostCreate() {

    }

    private fun setLanguage() {
        val language: String = PreferencesHelper.getString(PreferencesHelper.APP_LANGUAGE, Utils.getDefaultLanguage())
        val myLocale = Locale(language)
        Log.d("giangtd", "setLanguage: "+ myLocale)
        Utils.updateLocale(this, myLocale)
    }
}