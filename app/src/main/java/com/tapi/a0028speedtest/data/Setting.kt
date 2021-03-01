package com.tapi.a0028speedtest.data

import com.tapi.a0028speedtest.data.DataRateUnits


data class Setting(val testingUnits: DataRateUnits, val gaugeScale:Int){
    override fun toString(): String {
        return "DataRateUnits  $testingUnits  gaugeScale $gaugeScale"
    }
}