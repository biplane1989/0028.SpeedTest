package com.tapi.a0028speedtest.core.manager

import androidx.lifecycle.LiveData
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.data.DataRateUnits

interface SettingManager {
    fun getSetting(): LiveData<Setting>
    fun changeUnits(dataRateUnits: DataRateUnits)
    fun changeGaugeScale(dataRateUnits: DataRateUnits, value: Int)
}