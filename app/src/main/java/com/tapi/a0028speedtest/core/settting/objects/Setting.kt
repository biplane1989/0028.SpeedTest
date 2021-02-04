package com.tapi.a0028speedtest.core.settting.objects

import com.tapi.a0028speedtest.database.objects.DataRateUnits

data class Setting(val testingUnits: DataRateUnits, val gaugeScale:Int)