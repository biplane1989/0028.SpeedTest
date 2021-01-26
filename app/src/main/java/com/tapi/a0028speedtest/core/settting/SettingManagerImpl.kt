package com.tapi.a0028speedtest.core.settting

import androidx.lifecycle.LiveData
import com.tapi.a0028speedtest.core.manager.SettingManager
import com.tapi.a0028speedtest.core.settting.objects.DataRateUnits
import com.tapi.a0028speedtest.core.settting.objects.Setting

class SettingManagerImpl :SettingManager{
    override fun getSetting(): LiveData<Setting> {
        TODO("Not yet implemented")
    }

    override fun changeUnits(dataRateUnits: DataRateUnits) {
        TODO("Not yet implemented")
    }

    override fun changeGaugeScale(value: Int) {
        TODO("Not yet implemented")
    }
}