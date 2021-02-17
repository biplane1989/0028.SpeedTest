package com.tapi.a0028speedtest.functions.setting.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.base.SingleLiveEvent
import com.tapi.a0028speedtest.database.objects.DataRateUnits
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


    private val _onNetWorkSignalScanningSwitchIsSelected = SingleLiveEvent<Boolean>()
    val onNetWorkSignalScanningSwitchIsSelected: LiveData<Boolean> = _onNetWorkSignalScanningSwitchIsSelected

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

    fun clickedNetWorkSignalScanningSwitch() {
        ToastMessage.show("clickedNetWorkSignalScanningSwitch")
        _onNetWorkSignalScanningSwitchIsSelected.value = false

//        _onNetWorkSignalScanningSwitchClicked.value = true
    }

}