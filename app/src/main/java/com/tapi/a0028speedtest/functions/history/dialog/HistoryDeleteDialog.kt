package com.tapi.a0028speedtest.functions.history.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.databinding.HistoryDeleteDialogBinding
import com.tapi.a0028speedtest.functions.detail.dialog.HistoryDetailDeleteDialog

class HistoryDeleteDialog : BaseDialog(), View.OnClickListener {

    private var _binding: HistoryDeleteDialogBinding? = null
    private val binding: HistoryDeleteDialogBinding get() = _binding!!
    private var listener: HistoryDialogListener? = null

    companion object {
        val TAG = "LanguageDialog"

        @JvmStatic
        fun newInstance(listener: HistoryDialogListener): HistoryDeleteDialog {
            val dialog = HistoryDeleteDialog()
            dialog.listener = listener
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HistoryDeleteDialogBinding.inflate(inflater, container, false)
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
        binding.cancelTv.setOnClickListener(this)
        binding.okTv.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cancelTv -> {
                dismiss()
            }
            binding.okTv -> {
                listener?.onOKListener()
                dismiss()
            }
        }
    }

    interface HistoryDialogListener {
        fun onOKListener()
    }
}