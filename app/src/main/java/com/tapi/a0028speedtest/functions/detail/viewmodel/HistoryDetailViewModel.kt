package com.tapi.a0028speedtest.functions.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.core.settting.SettingManagerImpl
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.database.DBHelperFactory
import com.tapi.a0028speedtest.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryDetailViewModel : BaseViewModel() {

    private val _historyItem = MutableLiveData<History>()
    val historyItem: LiveData<History> get() = _historyItem

    val settingValue: LiveData<DataRateUnits> = SettingManagerImpl.getSetting().map {
        it.testingUnits
    }
    private val _settingType = SettingManagerImpl.settingType
    fun deleteItem(id: Int) {
        viewModelScope.launch {
            DBHelperFactory.getDBHelper().deleteHistory(id)
        }
    }

    fun getData(id: Int) {
        viewModelScope.launch {
            val history = DBHelperFactory.getDBHelper().getHistoryItemById(id)
            history?.let {
                it.downloadRate = Utils.convertDataRate(settingValue.value.toString(), it.downloadRate)
                it.updateRate = Utils.convertDataRate(settingValue.value.toString(), it.updateRate)

                _historyItem.value = it
            }
        }
    }
}