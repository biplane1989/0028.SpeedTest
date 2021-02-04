package com.tapi.a0028speedtest.functions.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.database.DBHelper
import com.tapi.a0028speedtest.database.objects.HistoryItem
import com.tapi.a0028speedtest.database.objects.NetworkType
import com.tapi.a0028speedtest.functions.main.objects.ConnectionType
import com.tapi.vpncore.objects.Host
import kotlinx.coroutines.*

object HistoryDatabase : DBHelper {

    private val _listHistoryItems = MutableLiveData<List<HistoryItem>>()

    init {
        CoroutineScope(Dispatchers.Default).launch {

            val floatsDownload = ArrayList<Float>()
            for (i in 1 until 100) {
                floatsDownload.add((i * 5..i * 10).random().toFloat())
            }

            val floatsUpload = ArrayList<Float>()
            for (i in 1 until 100) {
                floatsUpload.add((i * 5..i * 10).random().toFloat())
            }
            val hosts = Host("aa", "sss", "ss", "ss", "fff", "djsand")

            val listHistory = ArrayList<HistoryItem>()

            listHistory.add(HistoryItem(1, hosts, hosts, 5000 * 10000 , 1000 * 10000, 10, floatsDownload, floatsUpload, "12345", System.currentTimeMillis() - 1 * 1000 * 60 * 60 * 24, NetworkType.WIFI, ConnectionType.MULTI))
            listHistory.add(HistoryItem(21, hosts, hosts, 4000 * 10000, 3000 * 10000, 10, floatsDownload, floatsUpload, "12345", System.currentTimeMillis() - 1000 * 60 * 60 * 24, NetworkType.WIFI, ConnectionType.SINGLE))
            listHistory.add(HistoryItem(2, hosts, hosts, 6000 * 10000, 5000 * 10000, 10, floatsDownload, floatsUpload, "12345", 2 * 1000 * 60 * 60 * 24, NetworkType._2G, ConnectionType.MULTI))
            listHistory.add(HistoryItem(3, hosts, hosts, 8000 * 10000, 6000 * 10000, 10, floatsDownload, floatsUpload, "12345", 3 * 1000 * 60 * 60 * 24, NetworkType._3G, ConnectionType.SINGLE))
/*        listHistory.add(HistoryItem(3, hosts, hosts, 9000 * 10000, 7000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis()))
        listHistory.add(HistoryItem(4, hosts, hosts, 7000 * 10000, 4000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        listHistory.add(HistoryItem(5, hosts, hosts, 1000 * 10000, 9000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis() + 2 * 1000 * 60 * 60 * 24))
        listHistory.add(HistoryItem(6, hosts, hosts, 2000 * 10000, 1000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis() + 3 * 1000 * 60 * 60 * 24))
        listHistory.add(HistoryItem(7, hosts, hosts, 2000 * 10000, 1000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis() + 4 * 1000 * 60 * 60 * 24))
        listHistory.add(HistoryItem(8, hosts, hosts, 9000 * 10000, 1000 * 10000, 10, floats, floats, "12345", System.currentTimeMillis() + 5 * 1000 * 60 * 60 * 24))*/

            delay(3000)
            withContext(Dispatchers.Main) {
                _listHistoryItems.value = listHistory
            }

        }
    }

    override suspend fun saveHistory(item: HistoryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHistory(id: Int) {
        _listHistoryItems.value = _listHistoryItems.value?.filter { it.id !=  id}
    }

    override suspend fun deleteAllHistory() {
        withContext(Dispatchers.Main) {
            _listHistoryItems.value = emptyList()
        }
    }

    override fun getHistoryItems(): LiveData<List<HistoryItem>> {
        return _listHistoryItems
    }

    override fun getHistoryItemById(id: Int): HistoryItem? {
        for (item in _listHistoryItems.value!!) {
            if (item.id == id) return item
        }
        return null
    }
}