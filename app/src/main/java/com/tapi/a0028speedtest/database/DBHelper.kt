package com.tapi.a0028speedtest.database

import androidx.lifecycle.LiveData
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.database.entities.FavoriteEntity

interface DBHelper {
    suspend fun saveHistory(item: History)
    suspend fun saveHistory(item: List<History>)
    suspend fun deleteHistory(id: Int)
    suspend fun deleteAllHistory()
    fun getHistoryItems(): LiveData<List<History>>
    suspend fun getHistoryItemById(id: Int): History?

    suspend fun saveFavorite(item : FavoriteEntity)
    suspend fun deleteFavorite(ip: String)
    suspend fun getAllFavorite(): List<FavoriteEntity>
    suspend fun updateFavorite(item: FavoriteEntity)

}