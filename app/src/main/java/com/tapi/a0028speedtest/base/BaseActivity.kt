package com.tapi.a0028speedtest.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
        Log.d("giangtd", "setLanguage: " + myLocale)
        Utils.updateLocale(this, myLocale)
    }

    fun sendAction(actionName: String, data: Any?) {
        lifecycleScope.launchWhenResumed {
            if (!onActionReceived(actionName, data)) {
                supportFragmentManager.fragments.forEach {
                    sendActionToFragment(it, actionName, data)
                }
            }
        }
    }

    protected open fun onActionReceived(actionName: String, data: Any?): Boolean {
        return false
    }

    private fun sendActionToFragment(fragment: Fragment, actionName: String, data: Any?) {
        if (fragment is BaseFragment) {
            if (fragment.onActionReceived(actionName, data)) {
                return
            }
        }
        fragment.childFragmentManager.fragments.forEach {
            sendActionToFragment(it, actionName, data)
        }
    }

}