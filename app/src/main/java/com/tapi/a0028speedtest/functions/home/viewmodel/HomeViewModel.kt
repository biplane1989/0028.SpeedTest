package com.tapi.a0028speedtest.functions.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.base.BaseViewModel


enum class HomeButtonType {
    SPEEDTEST_TYPE, HISTORY_TYPE, SETTING_TYPE
}

enum class SelectType {
    SPEEDTEST, HISTORY, SETTING
}

class HomeViewModel : BaseViewModel() {

    private val _selectLivedata = MutableLiveData<SelectType>(SelectType.SPEEDTEST)
    val selectLivedata: LiveData<SelectType> get() = _selectLivedata

    fun clickedSpeedtestImageView() {
        _selectLivedata.value = SelectType.SPEEDTEST
    }

    fun clickedHistoryImageView() {
        _selectLivedata.value = SelectType.HISTORY
    }

    fun clickedSettingImageView() {
        _selectLivedata.value = SelectType.SETTING
    }

}