package com.tapi.a0028speedtest.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.tapi.a0028speedtest.database.objects.HistoryItem

interface DBHelper {
    suspend fun saveHistory(item: HistoryItem)
    suspend fun deleteHistory(id: Int)
    suspend fun deleteAllHistory()
    fun getHistoryItems(): LiveData<List<HistoryItem>>
    fun getHistoryItemById(id: Int): HistoryItem?
}