package com.tapi.a0028speedtest.core.settting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.core.manager.SettingManager
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.util.Constances
import com.tapi.a0028speedtest.util.PreferencesHelper

class SettingManagerImpl : SettingManager {

    private val _settingType = MutableLiveData<Setting>()
    val settingType: LiveData<Setting> get() = _settingType


    override fun getSetting(): LiveData<Setting> {
        return settingType
    }

    init {
        val default = PreferencesHelper.getString(Constances.SETTING_FIRST_TIME, "")
        if (default != "") {
            getData()
        } else {
            PreferencesHelper.putString(Constances.SETTING_FIRST_TIME, Constances.SETTING_FIRST_TIME_VALUE)
            PreferencesHelper.putString(Constances.SETTING_UNITS, Constances.SETING_MbPS)
            PreferencesHelper.putInt(Constances.SETING_MbPS_SCALE, Constances.SETTING_MBP_VALUE_100)
            PreferencesHelper.putInt(Constances.SETING_MBPS_SCALE, Constances.SETTING_MB_VALUE_10)
            PreferencesHelper.putInt(Constances.SETING_KBPS_SCALE, Constances.SETTING_KB_VALUE_5000)
            getData()
        }
    }

    override fun changeUnits(dataRateUnits: DataRateUnits) {
        when (dataRateUnits) {
            DataRateUnits.MbPS -> {
                PreferencesHelper.putString(Constances.SETTING_UNITS, Constances.SETING_MbPS)
            }
            DataRateUnits.MBPS -> {
                PreferencesHelper.putString(Constances.SETTING_UNITS, Constances.SETING_MBPS)
            }
            DataRateUnits.KBPS -> {
                PreferencesHelper.putString(Constances.SETTING_UNITS, Constances.SETING_KBPS)
            }
        }
        getData()
    }

    override fun changeGaugeScale(dataRateUnits: DataRateUnits, value: Int) {
        when (dataRateUnits) {
            DataRateUnits.MbPS -> {
                PreferencesHelper.putInt(Constances.SETING_MbPS_SCALE, value)
            }
            DataRateUnits.MBPS -> {
                PreferencesHelper.putInt(Constances.SETING_MBPS_SCALE, value)
            }
            DataRateUnits.KBPS -> {
                PreferencesHelper.putInt(Constances.SETING_KBPS_SCALE, value)
            }
        }
        getData()
    }

    private fun getData() {
        val settingUnits = PreferencesHelper.getString(Constances.SETTING_UNITS, "")
        when (settingUnits) {
            Constances.SETING_MbPS -> {
                _settingType.value = Setting(DataRateUnits.MbPS, PreferencesHelper.getInt(Constances.SETING_MbPS_SCALE, 0))
            }
            Constances.SETING_MBPS -> {
                _settingType.value = Setting(DataRateUnits.MBPS, PreferencesHelper.getInt(Constances.SETING_MBPS_SCALE, 0))
            }
            Constances.SETING_KBPS -> {
                _settingType.value = Setting(DataRateUnits.KBPS, PreferencesHelper.getInt(Constances.SETING_KBPS_SCALE, 0))
            }
        }
    }
}