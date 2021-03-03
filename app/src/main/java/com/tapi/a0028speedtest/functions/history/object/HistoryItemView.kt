package com.tapi.a0028speedtest.functions.history.`object`

import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.History

data class HistoryItemView(
        val history : History,
        var dataRateUnits: DataRateUnits
)
fun HistoryItemView.getDownloadRate(): Float {
    when (dataRateUnits) {
        DataRateUnits.MBPS -> {
            return history.downloadRate / (1024 * 1024f * 8)
        }
        DataRateUnits.MbPS -> {
            return history.downloadRate / (1024f * 1024)
        }
        DataRateUnits.KBPS -> {
            return history.downloadRate / (1024 * 8f)
        }
    }
}

fun HistoryItemView.getUpdateRate(): Float {
    when (dataRateUnits) {
        DataRateUnits.MBPS -> {
            return history.updateRate / (1024 * 1024f * 8)
        }
        DataRateUnits.MbPS -> {
            return history.updateRate / (1024f * 1024)
        }
        DataRateUnits.KBPS -> {
            return history.updateRate / (1024 * 8f)
        }
    }
}