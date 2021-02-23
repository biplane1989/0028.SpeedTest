package com.tapi.a0028speedtest.functions.setting

import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.home.viewmodel.SelectType
import com.tapi.a0028speedtest.ui.setting.DataRateUnitView

@BindingAdapter(value = ["is_selected"])
fun DataRateUnitView.setSelected(isSelected: Boolean) {
    Log.d("001", "isSelected: " + isSelected)
    this.background = if (isSelected) ContextCompat.getDrawable(this.context, R.drawable.setting_screen_bg_speed_tv) else null
}