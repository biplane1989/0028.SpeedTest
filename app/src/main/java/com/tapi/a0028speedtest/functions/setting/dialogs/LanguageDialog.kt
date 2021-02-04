package com.tapi.a0028speedtest.functions.setting.dialogs

import android.os.Bundle
import android.view.View
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import kotlinx.android.synthetic.main.setting_screen_language_dialog.*


class LanguageDialog : BaseDialog(), View.OnClickListener {

    var typeLanguage = ENGLISH_TYPE

    companion object {
        val TAG = "LanguageDialog"
        val VIET_NAM_TYPE = 1
        val ENGLISH_TYPE = 0
        val BUNDLE_NAME_KEY = "BUNDLE_NAME_KEY"
        lateinit var listener: LanguageDialogListener

        @JvmStatic
        fun newInstance(listener: LanguageDialogListener, typeLanguage: Int): LanguageDialog {
            this.listener = listener
            val dialog = LanguageDialog()
            val bundle = Bundle()
            bundle.putInt(BUNDLE_NAME_KEY, typeLanguage)
            dialog.arguments = bundle
            return dialog
        }
    }

//    override fun getLayoutResId(): Int {
//        return R.layout.setting_screen_language_dialog
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ok_tv.setOnClickListener(this)
        cancel_tv.setOnClickListener(this)
        vietnam_rb.setOnClickListener(this)
        english_rb.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            ok_tv -> {
                when (typeLanguage) {
                    VIET_NAM_TYPE -> {
                        listener.onOKListener(VIET_NAM_TYPE)
                    }
                    ENGLISH_TYPE -> {
                        listener.onOKListener(ENGLISH_TYPE)
                    }
                }
                dismiss()
            }
            cancel_tv -> {
                dismiss()
            }
            vietnam_rb -> {
                typeLanguage = VIET_NAM_TYPE
            }
            english_rb -> {
                typeLanguage = ENGLISH_TYPE
            }
        }
    }
}

interface LanguageDialogListener {
    fun onOKListener(typeLanguage: Int)
//    fun onCancelDialog()
}