package com.tapi.a0028speedtest.functions.history

import androidx.lifecycle.*
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.database.objects.HistoryItem
import kotlinx.coroutines.*

enum class SortType {
    TYPE_ASC, TYPE_DES, DATE_ASC, DATE_DSC, DOWNLOAD_ASC, DOWNLOAD_DSC, UPLOAD_ASC, UPLOAD_DSC
}

class HistoryViewModel : BaseViewModel() {

    private val _sortType = MutableLiveData<SortType>(SortType.TYPE_ASC)
    val sortType: LiveData<SortType> get() = _sortType

    private val _listHistory = MediatorLiveData<List<HistoryItem>>()
    val listHistory: LiveData<List<HistoryItem>> = _listHistory

    private val _listHistoryItem = HistoryDatabase.getHistoryItems()

    private var convertItemJob: Job? = null

//    private val _historyItems = MutableLiveData<List<HistoryItem>>()
//    val historyItems: LiveData<List<HistoryItem>> = TransformationsUnit.map(_historyItems, _sortType) { historyItems, type ->
//        when (type) {
//            SortType.DATE_ASC -> historyItems?.sortedBy { it.created }
//            SortType.DATE_DSC -> historyItems?.sortedByDescending { it.created }
//            SortType.DOWNLOAD_ASC -> historyItems?.sortedBy { it.downloadRate }
//            SortType.DOWNLOAD_DSC -> historyItems?.sortedByDescending { it.downloadRate }
//            SortType.UPLOAD_ASC -> historyItems?.sortedBy { it.updateRate }
//            SortType.UPLOAD_DSC -> historyItems?.sortedByDescending { it.updateRate }
//            else -> historyItems?.sortedBy { it.created }
//        } ?: emptyList()
//    }

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
            HistoryDatabase.deleteAllHistory()
        }
    }

}