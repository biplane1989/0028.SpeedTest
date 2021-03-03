package com.tapi.a0028speedtest.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment : Fragment() {

    internal open fun onActionReceived(actionName: String, data: Any?): Boolean {
        return false
    }

    protected fun sendAction(actionName: String, data: Any? = null) {
        lifecycleScope.launchWhenResumed {
            getBaseActivity()?.let {
                it.sendAction(actionName, data)
            }
        }
    }

    protected fun getBaseActivity(): BaseActivity? {
        if (activity != null) {
            return activity as BaseActivity
        }
        return null
    }
}