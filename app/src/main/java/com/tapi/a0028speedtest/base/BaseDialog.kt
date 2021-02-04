package com.tapi.a0028speedtest.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private const val DIALOG_STYLE_KEY = "DIALOG_STYLE_KEY"

abstract class BaseDialog : DialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.let { window ->
            arguments?.let {
                val style = it.getInt(DIALOG_STYLE_KEY, -1)
                if (style != -1) {
                    window.attributes.windowAnimations = style
                }
            }
        }
    }

    fun setStyle(style: Int) {
        arguments?.putInt(DIALOG_STYLE_KEY, style) ?: let {
            val bundle = Bundle()
            bundle.putInt(DIALOG_STYLE_KEY, style)
        }
    }
}