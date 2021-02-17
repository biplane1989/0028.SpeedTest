package com.tapi.a0028speedtest.functions.setting.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.SettingScreenBinding
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialog
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialogListener
import com.tapi.a0028speedtest.functions.setting.viewmodels.SettingViewModel
import com.tapi.a0028speedtest.util.ToastMessage


class SettingScreen : BaseFragment(), LanguageDialogListener, View.OnClickListener {

    private var _binding: SettingScreenBinding? = null
    private val binding get() = _binding!!
    val TAG = "giangtd"
    private val settingViewModel: SettingViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = SettingScreenBinding.inflate(inflater, container, false)
        binding.viewmodel = settingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListenner()
    }

    private fun initListenner() {
        binding.learnMoreTv.setOnClickListener(this)
        binding.adchoiceTv.setOnClickListener(this)
        binding.restorePurcahseIv.setOnClickListener(this)
        binding.removeAdsIv.setOnClickListener(this)
        binding.speedtestHelpIv.setOnClickListener(this)
        binding.languageIv.setOnClickListener(this)
        binding.giveUsFeedbackIv.setOnClickListener(this)
        binding.aboutIv.setOnClickListener(this)
        binding.privacyPolicyIv.setOnClickListener(this)
        binding.termsIv.setOnClickListener(this)
        binding.rateIv.setOnClickListener(this)
        binding.versionIv.setOnClickListener(this)
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
        fun newInstance(): SettingScreen {
            val settingScreen = SettingScreen()
            return settingScreen
        }
    }

    override fun onOKListener(typeLanguage: Int) {      // ok listener Language dialog

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.learnMoreTv -> {
                ToastMessage.show("learnMoreTv")
            }
            binding.adchoiceTv -> {
                ToastMessage.show("adchoiceTv")
            }
            binding.restorePurcahseIv -> {
                ToastMessage.show("restorePurcahseIv")
            }
            binding.removeAdsIv -> {
                ToastMessage.show("removeAdsIv")
            }
            binding.speedtestHelpIv -> {
                ToastMessage.show("speedtestHelpIv")
            }
            binding.languageIv -> {
                showLanguageDialog()
            }
            binding.giveUsIv -> {
                ToastMessage.show("giveUsIv")
            }
            binding.aboutIv -> {
                ToastMessage.show("aboutIv")
            }
            binding.privacyPolicyIv -> {
                ToastMessage.show("privacyPolicyIv")
            }
            binding.termsIv -> {
                ToastMessage.show("termsIv")
            }
            binding.rateIv -> {
                ToastMessage.show("rateIv")
            }
            binding.versionIv -> {
                ToastMessage.show("versionIv")
            }
        }
    }

}