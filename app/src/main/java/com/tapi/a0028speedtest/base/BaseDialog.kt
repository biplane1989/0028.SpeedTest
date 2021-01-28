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
    protected abstract fun getLayoutResId(): Int
    override fun show(manager: FragmentManager, tag: String?) {
        validateDialogTag(tag)
        super.show(manager, tag)
    }
    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        TODO("not implemented")
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        validateDialogTag(tag)
        super.showNow(manager, tag)
    }

    private fun validateDialogTag(tag: String?) {
        tag?.let {
            if (it.isEmpty()) {
                throw IllegalArgumentException("tag is not empty")
            }
        } ?: throw IllegalArgumentException("tag is not null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(getLayoutResId(), container, false)
        initViews(view, savedInstanceState)
        bindEvents()
        return view
    }

    open protected fun initViews(view: View, savedInstanceState: Bundle?) {

    }

    open protected fun bindEvents() {

    }

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