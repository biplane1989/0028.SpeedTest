package com.tapi.a0028speedtest.base

import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {
    protected open fun onPreCreate(): Boolean {
        return true
    }

    protected open fun onPostCreate() {

    }
}