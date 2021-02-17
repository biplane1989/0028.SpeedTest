package com.tapi.a0028speedtest.functions.home

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.databinding.BindingAdapter
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.home.viewmodel.HomeButtonType
import com.tapi.a0028speedtest.functions.home.viewmodel.SelectType
import com.tapi.a0028speedtest.ui.home.HomeImageView

@BindingAdapter(value = ["selected", "type"])
fun HomeImageView.setSelected(selected: SelectType, type: HomeButtonType) {
    when (selected) {
        SelectType.SPEEDTEST -> {
            if (type == HomeButtonType.SPEEDTEST_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_blue)))
            }
            if (type == HomeButtonType.HISTORY_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }
            if (type == HomeButtonType.SETTING_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }
        }
        SelectType.HISTORY -> {
            if (type == HomeButtonType.HISTORY_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_blue)))
            }
            if (type == HomeButtonType.SETTING_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }
            if (type == HomeButtonType.SPEEDTEST_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }

        }
        SelectType.SETTING -> {
            if (type == HomeButtonType.SETTING_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_blue)))
            }
            if (type == HomeButtonType.SPEEDTEST_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }
            if (type == HomeButtonType.HISTORY_TYPE) {
                this.setColorFilter(Color.parseColor(context.getString(R.string.home_screen_color_tab_white)))
            }
        }
    }
}