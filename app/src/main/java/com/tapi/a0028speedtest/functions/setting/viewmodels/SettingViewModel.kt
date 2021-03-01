package com.tapi.a0028speedtest.functions.setting.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.base.SingleLiveEvent
import com.tapi.a0028speedtest.core.manager.SettingManager
import com.tapi.a0028speedtest.core.settting.SettingManagerImpl
import com.tapi.a0028speedtest.util.Constances
import com.tapi.a0028speedtest.util.ToastMessage
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.util.PreferencesHelper
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel() {

    val TAG = "giangtd"


    private var settingUnitType = PreferencesHelper.getString(Constances.SETTING_UNITS, DataRateUnits.MbPS.toString())

    val settingManager: SettingManager = SettingManagerImpl

    private val _mbpValue = MutableLiveData<String>()
    val mbpValue: LiveData<String> get() = _mbpValue

    private val _mbValue = MutableLiveData<String>()
    val mbValue: LiveData<String> get() = _mbValue

    private val _kbValue = MutableLiveData<String>()
    val kbValue: LiveData<String> get() = _kbValue

    private val _onNetWorkSignalScanningSwitchIsSelected = SingleLiveEvent<Boolean>()
    val onNetWorkSignalScanningSwitchIsSelected: LiveData<Boolean> = _onNetWorkSignalScanningSwitchIsSelected

    val speedLiveData: LiveData<DataRateUnits>              // tranfrom tu 1 livedata nay sang 1 livedata khac
        get() = settingManager.getSetting().map {
            setDataScale(it.testingUnits.toString())
            it.testingUnits
        }

    val speedValueLiveData: LiveData<DataRateUnits>
        get() = settingManager.getSetting().map {
            var scaleType = DataRateUnits.MbPS
            Log.d("001", "data get : " + it.gaugeScale)
            when (it.gaugeScale) {
                Constances.SETTING_MBP_VALUE_100 -> scaleType = DataRateUnits.MbPS
                Constances.SETTING_MBP_VALUE_500 -> scaleType = DataRateUnits.MBPS
                Constances.SETTING_MBP_VALUE_1000 -> scaleType = DataRateUnits.KBPS
                Constances.SETTING_MB_VALUE_10 -> scaleType = DataRateUnits.MbPS
                Constances.SETTING_MB_VALUE_50 -> scaleType = DataRateUnits.MBPS
                Constances.SETTING_MB_VALUE_100 -> scaleType = DataRateUnits.KBPS
                Constances.SETTING_KB_VALUE_5000 -> scaleType = DataRateUnits.MbPS
                Constances.SETTING_KB_VALUE_10000 -> scaleType = DataRateUnits.MBPS
                Constances.SETTING_KB_VALUE_15000 -> scaleType = DataRateUnits.KBPS
            }
            Log.d("001", "scaleType : " + scaleType)
            scaleType
        }

    init {
        val settingUnits = PreferencesHelper.getString(Constances.SETTING_UNITS, "")
        setDataScale(settingUnits)
    }

    private fun setDataScale(settingUnits: String) {
        when (settingUnits) {
            Constances.SETING_MbPS -> {
                _mbpValue.value = Constances.SETTING_SCREEN_MBP_VALUE_1
                _mbValue.value = Constances.SETTING_SCREEN_MBP_VALUE_2
                _kbValue.value = Constances.SETTING_SCREEN_MBP_VALUE_3
            }
            Constances.SETING_MBPS -> {
                _mbpValue.value = Constances.SETTING_SCREEN_MB_VALUE_1
                _mbValue.value = Constances.SETTING_SCREEN_MB_VALUE_2
                _kbValue.value = Constances.SETTING_SCREEN_MB_VALUE_3
            }
            Constances.SETING_KBPS -> {
                _mbpValue.value = Constances.SETTING_SCREEN_KB_VALUE_1
                _mbValue.value = Constances.SETTING_SCREEN_KB_VALUE_2
                _kbValue.value = Constances.SETTING_SCREEN_KB_VALUE_3
            }
        }
    }

    fun onClickMbpSeepd() {
        settingUnitType = DataRateUnits.MbPS.toString()
        settingManager.changeUnits(DataRateUnits.MbPS)
    }

    fun onClickMbSeepd() {
        settingUnitType = DataRateUnits.MBPS.toString()
        settingManager.changeUnits(DataRateUnits.MBPS)
    }

    fun onClickKbSpeed() {
        settingUnitType = DataRateUnits.KBPS.toString()
        settingManager.changeUnits(DataRateUnits.KBPS)
    }

    fun onClickMbpValueSeepd() {
        when (settingUnitType) {
            DataRateUnits.MbPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MbPS, Constances.SETTING_MBP_VALUE_100)
            }
            DataRateUnits.MBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MBPS, Constances.SETTING_MB_VALUE_10)
            }
            DataRateUnits.KBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.KBPS, Constances.SETTING_KB_VALUE_5000)
            }
        }
    }

    fun onClickMbValueSeepd() {
        when (settingUnitType) {
            DataRateUnits.MbPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MbPS, Constances.SETTING_MBP_VALUE_500)
            }
            DataRateUnits.MBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MBPS, Constances.SETTING_MB_VALUE_50)
            }
            DataRateUnits.KBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.KBPS, Constances.SETTING_KB_VALUE_10000)
            }
        }
    }

    fun onClickKbValueSeepd() {
        when (settingUnitType) {
            DataRateUnits.MbPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MbPS, Constances.SETTING_MBP_VALUE_1000)
            }
            DataRateUnits.MBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.MBPS, Constances.SETTING_MB_VALUE_100)
            }
            DataRateUnits.KBPS.toString() -> {
                settingManager.changeGaugeScale(DataRateUnits.KBPS, Constances.SETTING_KB_VALUE_15000)
            }
        }
    }

    fun clickedNetWorkSignalScanningSwitch() {
        ToastMessage.show("clickedNetWorkSignalScanningSwitch")
        _onNetWorkSignalScanningSwitchIsSelected.value = false

//        _onNetWorkSignalScanningSwitchClicked.value = true
    }

}