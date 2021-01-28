package com.tapi.a0028speedtest.functions.setting.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.FragmentSettingScreenBinding
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialog
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialogListener
import com.tapi.a0028speedtest.functions.setting.viewmodels.SettingViewModel


class SettingScreen : BaseFragment(), LanguageDialogListener {

    private var _binding: FragmentSettingScreenBinding? = null
    private val binding get() = _binding!!
    val TAG = "giangtd"
    private val settingViewModel: SettingViewModel by viewModels()

    val onLearnMoreObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onLearnMoreObserver ")
        }
    }

    val onAdchoiceObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onAdchoiceObserver ")
        }
    }

    val onRestonePurchaseObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onRestonePurchaseObserver ")
        }
    }
    val onclickOnRemoveADSObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onclickOnRemoveADSObserver ")
        }
    }

    val onclickOnSpeedtestHelpObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onclickOnSpeedtestHelpObserver ")
        }
    }

    val onclickOnLanguageObserver = Observer<Boolean> {
        if (it) {
            showLanguageDialog()
        }
    }

    val onclickOnGiveUsFeedbackObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onclickOnGiveUsFeedbackObserver ")
        }
    }

    val onclickOnAboutSpeedtestObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onclickOnAboutSpeedtestObserver ")
        }
    }
    val onPrivacyPolicyObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onPrivacyPolicyObserver ")
        }
    }

    val onTermsOfUseObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onTermsOfUseObserver ")
        }
    }

    val onRateAppObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onRateAppObserver ")
        }
    }

    val onVersionObserver = Observer<Boolean> {
        if (it) {
            Log.d(TAG, "onVersionObserver ")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSettingScreenBinding.inflate(inflater, container, false)
        binding.viewmodel = settingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        observerData()

        return binding.root
    }

    private fun observerData() {
        settingViewModel.onLearnMoreButtonClicked.observe(viewLifecycleOwner, onLearnMoreObserver)
        settingViewModel.onAdchoiceButtonClicked.observe(viewLifecycleOwner, onAdchoiceObserver)
        settingViewModel.onRestonePurchaseButtonClicked.observe(viewLifecycleOwner, onRestonePurchaseObserver)
        settingViewModel.onRemoveADSButtonClicked.observe(viewLifecycleOwner, onclickOnRemoveADSObserver)
        settingViewModel.onSpeedtestHelpButtonClicked.observe(viewLifecycleOwner, onclickOnSpeedtestHelpObserver)
        settingViewModel.onLanguageButtonClicked.observe(viewLifecycleOwner, onclickOnLanguageObserver)
        settingViewModel.onGiveUsFeedbackButtonClicked.observe(viewLifecycleOwner, onclickOnGiveUsFeedbackObserver)
        settingViewModel.onAboutSpeedtestButtonClicked.observe(viewLifecycleOwner, onclickOnAboutSpeedtestObserver)
        settingViewModel.onPrivacyPolicyButtonClicked.observe(viewLifecycleOwner, onPrivacyPolicyObserver)
        settingViewModel.onTermsOfUseButtonClicked.observe(viewLifecycleOwner, onTermsOfUseObserver)
        settingViewModel.onRateAppButtonClicked.observe(viewLifecycleOwner, onRateAppObserver)
        settingViewModel.onVersionButtonClicked.observe(viewLifecycleOwner, onVersionObserver)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showLanguageDialog() {
        val languageDialog = LanguageDialog.newInstance(this, LanguageDialog.ENGLISH_TYPE)
        languageDialog.show(childFragmentManager, LanguageDialog.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = SettingScreen().apply {}
    }

    override fun onOKListener(typeLanguage: Int) {      // ok listener Language dialog

    }

}