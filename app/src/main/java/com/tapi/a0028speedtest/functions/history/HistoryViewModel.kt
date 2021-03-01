package com.tapi.a0028speedtest.functions.history

import android.util.Log
import androidx.lifecycle.*
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.data.NetworkType
import com.tapi.a0028speedtest.database.DBHelper
import com.tapi.a0028speedtest.database.DBHelperFactory
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.vpncore.objects.Host
import kotlinx.coroutines.*

enum class SortType {
    TYPE_ASC, TYPE_DES, DATE_ASC, DATE_DSC, DOWNLOAD_ASC, DOWNLOAD_DSC, UPLOAD_ASC, UPLOAD_DSC
}

class HistoryViewModel : BaseViewModel() {
    private val dbHelper: DBHelper = DBHelperFactory.getDBHelper()
    private val _sortType = MutableLiveData<SortType>(SortType.TYPE_ASC)
    val sortType: LiveData<SortType> get() = _sortType

    private val _listHistory = MediatorLiveData<List<History>>()
    val listHistory: LiveData<List<History>> = _listHistory

    private val _listHistoryItem = dbHelper.getHistoryItems()

    private var convertItemJob: Job? = null

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> get() = _loading

    val empty = Transformations.map(listHistory) {
        it == null || it.isEmpty()
    }

    init {
        _listHistory.addSource(_sortType) {
            convertItemJob?.cancel()
            convertItemJob = viewModelScope.launch {
                convertListHistoryViewItem()
            }
        }
        _listHistory.addSource(_listHistoryItem) {
//            _loading.value = true
            convertItemJob?.cancel()
            convertItemJob = viewModelScope.launch {
                convertListHistoryViewItem()
            }
        }
    }

    private suspend fun convertListHistoryViewItem() = withContext(Dispatchers.Default) {
        _listHistoryItem.value?.let {

            var listItem = it
            listItem = when (sortType.value) {
                SortType.TYPE_ASC -> listItem.sortedBy { it.networkType }
                SortType.TYPE_DES -> listItem.sortedByDescending { it.networkType }
                SortType.DATE_ASC -> listItem.sortedBy { it.created }
                SortType.DATE_DSC -> listItem.sortedByDescending { it.created }
                SortType.DOWNLOAD_ASC -> listItem.sortedBy { it.downloadRate }
                SortType.DOWNLOAD_DSC -> listItem.sortedByDescending { it.downloadRate }
                SortType.UPLOAD_ASC -> listItem.sortedBy { it.updateRate }
                SortType.UPLOAD_DSC -> listItem.sortedByDescending { it.updateRate }
                else -> listItem.sortedBy { it.created }
            }

            if (isActive) {
                withContext(Dispatchers.Main) {
                    _listHistory.value = listItem
                    if (_loading.value != false) {
                        _loading.value = false
                    }
                }
            }
            for (item in listItem) {
                Log.d("giangtd", "item id: " + item.id)
            }
        }
    }


    fun sortType() {
        if (_sortType.value == SortType.TYPE_ASC) _sortType.value = SortType.TYPE_DES else _sortType.value = SortType.TYPE_ASC
    }

    fun sortDate() {
        if (_sortType.value == SortType.DATE_ASC) _sortType.value = SortType.DATE_DSC else _sortType.value = SortType.DATE_ASC
    }

    fun sortDownload() {
        if (_sortType.value == SortType.DOWNLOAD_ASC) _sortType.value = SortType.DOWNLOAD_DSC else _sortType.value = SortType.DOWNLOAD_ASC
    }

    fun sortUpload() {
        if (_sortType.value == SortType.UPLOAD_ASC) _sortType.value = SortType.UPLOAD_DSC else _sortType.value = SortType.UPLOAD_ASC
    }


    fun deleteAllItem() {
        viewModelScope.launch(Dispatchers.Default) {
            dbHelper.deleteAllHistory()
        }
    }

}