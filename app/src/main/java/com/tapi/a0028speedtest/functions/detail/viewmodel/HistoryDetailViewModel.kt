package com.tapi.a0028speedtest.functions.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.functions.history.HistoryDatabase
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.database.DBHelperFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryDetailViewModel : BaseViewModel() {

    private val _historyItem = MutableLiveData<History>()
    val historyItem: LiveData<History> get() = _historyItem

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            DBHelperFactory.getDBHelper().deleteHistory(id)
        }
    }

    fun getData(id: Int) {
        viewModelScope.launch {
            _historyItem.value = DBHelperFactory.getDBHelper().getHistoryItemById(id)
        }
    }
}