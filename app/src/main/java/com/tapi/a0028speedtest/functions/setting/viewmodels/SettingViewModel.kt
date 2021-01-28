package com.tapi.a0028speedtest.functions.setting.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.base.SingleLiveEvent
import com.tapi.a0028speedtest.core.settting.objects.DataRateUnits
import com.tapi.a0028speedtest.util.Constances
import com.tapi.a0028speedtest.util.ToastMessage

class SettingViewModel : BaseViewModel() {

    val TAG = "giangtd"


    private val _mbpValue = MutableLiveData<String>()
    val mbpValue: LiveData<String> get() = _mbpValue

    private val _mbValue = MutableLiveData<String>()
    val mbValue: LiveData<String> get() = _mbValue

    private val _kbValue = MutableLiveData<String>()
    val kbValue: LiveData<String> get() = _kbValue


    private val _speedLiveData = MutableLiveData<DataRateUnits>()
    val speedLiveData: LiveData<DataRateUnits> get() = _speedLiveData

    private val _speedValueLiveData = MutableLiveData<DataRateUnits>()
    val speedValueLiveData: LiveData<DataRateUnits> get() = _speedValueLiveData

    private val _onLearnMoreButtonClicked = SingleLiveEvent<Boolean>()
    val onLearnMoreButtonClicked: LiveData<Boolean> = _onLearnMoreButtonClicked

    private val _onAdchoiceButtonClicked = SingleLiveEvent<Boolean>()
    val onAdchoiceButtonClicked: LiveData<Boolean> = _onAdchoiceButtonClicked

    private val _onRestonePurchaseButtonClicked = SingleLiveEvent<Boolean>()
    val onRestonePurchaseButtonClicked: LiveData<Boolean> = _onRestonePurchaseButtonClicked

    private val _onRemoveADSButtonClicked = SingleLiveEvent<Boolean>()
    val onRemoveADSButtonClicked: LiveData<Boolean> = _onRemoveADSButtonClicked

    private val _onSpeedtestHelpButtonClicked = SingleLiveEvent<Boolean>()
    val onSpeedtestHelpButtonClicked: LiveData<Boolean> = _onSpeedtestHelpButtonClicked

    private val _onLanguageButtonClicked = SingleLiveEvent<Boolean>()
    val onLanguageButtonClicked: LiveData<Boolean> = _onLanguageButtonClicked

    private val _onGiveUsFeedbackButtonClicked = SingleLiveEvent<Boolean>()
    val onGiveUsFeedbackButtonClicked: LiveData<Boolean> = _onGiveUsFeedbackButtonClicked

    private val _onAboutSpeedtestButtonClicked = SingleLiveEvent<Boolean>()
    val onAboutSpeedtestButtonClicked: LiveData<Boolean> = _onAboutSpeedtestButtonClicked

    private val _onPrivacyPolicyButtonClicked = SingleLiveEvent<Boolean>()
    val onPrivacyPolicyButtonClicked: LiveData<Boolean> = _onPrivacyPolicyButtonClicked

    private val _onTermsOfUseButtonClicked = SingleLiveEvent<Boolean>()
    val onTermsOfUseButtonClicked: LiveData<Boolean> = _onTermsOfUseButtonClicked

    private val _onRateAppButtonClicked = SingleLiveEvent<Boolean>()
    val onRateAppButtonClicked: LiveData<Boolean> = _onRateAppButtonClicked

    private val _onVersionButtonClicked = SingleLiveEvent<Boolean>()
    val onVersionButtonClicked: LiveData<Boolean> = _onVersionButtonClicked

    private val _onNetWorkSignalScanningSwitchIsSelected = SingleLiveEvent<Boolean>()
    val onNetWorkSignalScanningSwitchIsSelected: LiveData<Boolean> = _onNetWorkSignalScanningSwitchIsSelected

    init {

    }

    fun mbpSeepdOnclick() {
        _mbpValue.value = Constances.SETTING_SCREEN_MBP_VALUE_1
        _mbValue.value = Constances.SETTING_SCREEN_MBP_VALUE_2
        _kbValue.value = Constances.SETTING_SCREEN_MBP_VALUE_3

        _speedLiveData.value = DataRateUnits.MbPS
    }

    fun mbSeepdOnclick() {
        _mbpValue.value = Constances.SETTING_SCREEN_MB_VALUE_1
        _mbValue.value = Constances.SETTING_SCREEN_MB_VALUE_2
        _kbValue.value = Constances.SETTING_SCREEN_MB_VALUE_3

        _speedLiveData.value = DataRateUnits.MBPS
    }

    fun kbSpeedOnclick() {
        _mbpValue.value = Constances.SETTING_SCREEN_KB_VALUE_1
        _mbValue.value = Constances.SETTING_SCREEN_KB_VALUE_2
        _kbValue.value = Constances.SETTING_SCREEN_KB_VALUE_3

        _speedLiveData.value = DataRateUnits.KBPS
    }

    fun mbpValueSeepdOnclick() {

        _speedValueLiveData.value = DataRateUnits.MbPS
    }

    fun mbValueSeepdOnclick() {
        _speedValueLiveData.value = DataRateUnits.MBPS
    }

    fun kbValueSeepdOnclick() {
        _speedValueLiveData.value = DataRateUnits.KBPS
    }

    fun clickedLearnMoreButton() {
        _onLearnMoreButtonClicked.value = true
    }

    fun clickOnAdchoiceButton() {
        _onAdchoiceButtonClicked.value = true
    }

    fun clickOnRestonePurchaseButton() {
        _onRestonePurchaseButtonClicked.value = true
    }

    fun clickOnRemoveADSButton() {
        _onRemoveADSButtonClicked.value = true
    }

    fun clickOnSpeedTestHelpButton() {
        _onSpeedtestHelpButtonClicked.value = true
    }

    fun clickOnLanguageButton() {
        _onLanguageButtonClicked.value = true
    }

    fun clickOnGiveUsFeedbackButton() {
        _onGiveUsFeedbackButtonClicked.value = true
    }

    fun clickOnAboutSpeedTestButton() {
        _onAboutSpeedtestButtonClicked.value = true
    }

    fun clickedOnPrivacyPolicyButton() {
        _onPrivacyPolicyButtonClicked.value = true
    }

    fun clickedOnTermsOfUseButton() {
        _onTermsOfUseButtonClicked.value = true
    }

    fun clickedOnRateAppButton() {
        _onRateAppButtonClicked.value = true
    }

    fun clickedOnVersionButton() {
        _onVersionButtonClicked.value = true
    }

    fun clickedNetWorkSignalScanningSwitch() {
        ToastMessage.show("clickedNetWorkSignalScanningSwitch")
        _onNetWorkSignalScanningSwitchIsSelected.value = false

//        _onNetWorkSignalScanningSwitchClicked.value = true
    }

}