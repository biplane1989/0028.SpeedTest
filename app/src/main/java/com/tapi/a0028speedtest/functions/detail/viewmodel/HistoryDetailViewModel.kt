package com.tapi.a0028speedtest.functions.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.database.objects.HistoryItem
import com.tapi.a0028speedtest.functions.history.HistoryDatabase
import kotlinx.coroutines.launch

class HistoryDetailViewModel : BaseViewModel() {

    private val _historyItem = MutableLiveData<HistoryItem>()
    val historyItem: LiveData<HistoryItem> get() = _historyItem

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            HistoryDatabase.deleteHistory(id)
        }
    }

    fun getData(id: Int) {
        _historyItem.value = HistoryDatabase.getHistoryItemById(id)
    }
}