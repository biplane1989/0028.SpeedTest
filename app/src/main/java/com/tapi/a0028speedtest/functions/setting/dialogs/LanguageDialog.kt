package com.tapi.a0028speedtest.functions.setting.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.databinding.HistoryDetailDeleteDialogBinding
import com.tapi.a0028speedtest.databinding.SettingScreenLanguageDialogBinding
import com.tapi.a0028speedtest.functions.detail.dialog.HistoryDetailDeleteDialog
import kotlinx.android.synthetic.main.setting_screen_language_dialog.*


class LanguageDialog : BaseDialog(), View.OnClickListener {

    var typeLanguage = ENGLISH_TYPE
    private var _binding: SettingScreenLanguageDialogBinding? = null
    private val binding: SettingScreenLanguageDialogBinding get() = _binding!!
    private var listener: LanguageDialogListener? = null

    companion object {
        val TAG = "LanguageDialog"
        val VIET_NAM_TYPE = 1
        val ENGLISH_TYPE = 0
        val BUNDLE_NAME_KEY = "BUNDLE_NAME_KEY"
        @JvmStatic
        fun newInstance(listener: LanguageDialogListener, typeLanguage: Int): LanguageDialog {
            val dialog = LanguageDialog()
            dialog.listener = listener
            val bundle = Bundle()
            bundle.putInt(BUNDLE_NAME_KEY, typeLanguage)
            dialog.arguments = bundle
            return dialog
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SettingScreenLanguageDialogBinding.inflate(inflater, container, false)
        if (listener == null) {
            dismiss()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.okTv.setOnClickListener(this)
        binding.cancelTv.setOnClickListener(this)
        binding.vietnamRb.setOnClickListener(this)
        binding.englishRb.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.okTv -> {
                when (typeLanguage) {
                    VIET_NAM_TYPE -> {
                        listener?.onOKListener(VIET_NAM_TYPE)
                    }
                    ENGLISH_TYPE -> {
                        listener?.onOKListener(ENGLISH_TYPE)
                    }
                }
                dismiss()
            }
            binding.cancelTv -> {
                dismiss()
            }
            binding.vietnamRb -> {
                typeLanguage = VIET_NAM_TYPE
            }
            binding.englishRb -> {
                typeLanguage = ENGLISH_TYPE
            }
        }
    }
}

interface LanguageDialogListener {
    fun onOKListener(typeLanguage: Int)
}