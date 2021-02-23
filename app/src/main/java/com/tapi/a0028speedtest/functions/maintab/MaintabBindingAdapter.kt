package com.tapi.a0028speedtest.functions.maintab

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.data.DataRateUnits


@BindingAdapter("bg_select")
fun checkSelect(view: View, rs: Boolean) {
    val mTv = view as TextView
    if (rs) {
        mTv.setTextColor(ContextCompat.getColor(mTv.context, R.color.white))
    } else {
        mTv.setTextColor(ContextCompat.getColor(mTv.context, R.color.colorGrayText))
    }
}

@BindingAdapter("unit_tv")
fun checkUnit(view: View, rateUnits: DataRateUnits) {
    val tv = view as TextView
    when (rateUnits) {
        DataRateUnits.KBPS -> {
            tv.text = "Kbs"
        }
        DataRateUnits.MbPS -> {
            tv.text = "Mbps"
        }
        DataRateUnits.MBPS -> {
            tv.text = "Mbs"
        }
    }
}

