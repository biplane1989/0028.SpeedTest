package com.tapi.a0028speedtest.functions.setting.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.SettingScreenBinding
import com.tapi.a0028speedtest.functions.common.ThankDialog
import com.tapi.a0028speedtest.functions.common.VipDialogFragment
import com.tapi.a0028speedtest.functions.detail.dialog.HistoryDetailDeleteDialog
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialog
import com.tapi.a0028speedtest.functions.setting.dialogs.LanguageDialogListener
import com.tapi.a0028speedtest.functions.setting.viewmodels.SettingViewModel
import com.tapi.a0028speedtest.util.PreferencesHelper
import com.tapi.a0028speedtest.util.ToastMessage
import com.tapi.a0028speedtest.util.Utils
import java.util.*


class SettingScreen : BaseFragment(), LanguageDialogListener, View.OnClickListener, ThankDialog.ThankDialogListener {

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
        if (Utils.getDefaultLanguage().equals("vi")) {
            binding.languageTv.text = resources.getString(R.string.setting_screen_language_dialog_viet_nam)
        } else {
            binding.languageTv.text = resources.getString(R.string.setting_screen_language_dialog_english)
        }
    }

    private fun initListenner() {
        binding.learnMoreTv.setOnClickListener(this)
        binding.adchoiceTv.setOnClickListener(this)
        binding.restoreIv.setOnClickListener(this)
        binding.removeIv.setOnClickListener(this)
        binding.speedtestHelpIv.setOnClickListener(this)
        binding.languageIv.setOnClickListener(this)
        binding.giveUsIv.setOnClickListener(this)
        binding.aboutIv.setOnClickListener(this)
        binding.privacyPolicyIv.setOnClickListener(this)
        binding.termsIv.setOnClickListener(this)
        binding.rateIv.setOnClickListener(this)
        binding.versionIv.setOnClickListener(this)
        binding.ivVipic.setOnClickListener(this)
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
        when (typeLanguage) {
            LanguageDialog.ENGLISH_TYPE -> {
                PreferencesHelper.putString(PreferencesHelper.APP_LANGUAGE, "en")
                val myLocale = Locale("en")
                Utils.updateLocale(requireContext(), myLocale)
                binding.languageTv.text = resources.getString(R.string.setting_screen_language_dialog_english)
                requireActivity().recreate()
            }
            LanguageDialog.VIET_NAM_TYPE -> {
                PreferencesHelper.putString(PreferencesHelper.APP_LANGUAGE, "vi")
                val myLocale = Locale("vi")
                Utils.updateLocale(requireContext(), myLocale)
                binding.languageTv.text = resources.getString(R.string.setting_screen_language_dialog_viet_nam)
                requireActivity().recreate()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.learnMoreTv -> {
                ToastMessage.show("learnMoreTv")
            }
            binding.adchoiceTv -> {
                ToastMessage.show("adchoiceTv")
            }
            binding.restoreIv -> {

                if (childFragmentManager.findFragmentByTag(VipDialogFragment.TAG) == null) {
                    val dialog = VipDialogFragment()
                    dialog.show(childFragmentManager, VipDialogFragment.TAG)
                }
            }
            binding.removeIv -> {
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
                if (childFragmentManager.findFragmentByTag(ThankDialog.TAG) == null) {
                    val dialog = ThankDialog(this)
                    dialog.show(childFragmentManager, ThankDialog.TAG)
                }
            }
            binding.versionIv -> {
                ToastMessage.show("versionIv")
            }
            binding.ivVipic -> {
                if (childFragmentManager.findFragmentByTag(VipDialogFragment.TAG) == null) {
                    val dialog = VipDialogFragment()
                    dialog.show(childFragmentManager, VipDialogFragment.TAG)
                }
            }
        }
    }

    //todo
    override fun actionYes() {
        Log.d(TAG, "actionUpgate: actionYes")
    }

    //todo
    override fun actionNo() {
        Log.d(TAG, "actionUpgate: actionNo")
    }

}