package com.tapi.a0028speedtest.core.manager

import androidx.lifecycle.LiveData
import com.tapi.a0028speedtest.core.settting.objects.DataRateUnits
import com.tapi.a0028speedtest.core.settting.objects.Setting

interface SettingManager {
    fun getSetting():LiveData<Setting>
    fun changeUnits(dataRateUnits: DataRateUnits)
    fun changeGaugeScale(value:Int)
}