package com.tapi.a0028speedtest.functions.history

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.ui.history.HistoryTitleTextView
import com.tapi.a0028speedtest.ui.setting.DataRateUnitView

@BindingAdapter(value = ["text_value"])
fun HistoryTitleTextView.setText(dataRateUnits: DataRateUnits) {
    when(dataRateUnits){
        DataRateUnits.MbPS-> {
            this.text = "Mbps"
        }
        DataRateUnits.MBPS ->{
            this.text = "MB/s"
        }
        DataRateUnits.KBPS->{
            this.text = "KB/s"
        }
    }
}