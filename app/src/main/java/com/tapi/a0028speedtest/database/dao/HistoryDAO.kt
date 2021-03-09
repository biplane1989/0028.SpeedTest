package com.tapi.a0028speedtest.database.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tapi.a0028speedtest.database.entities.HistoryEntity

@Dao
interface HistoryDAO {

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getHistoryById(id: Int): HistoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: List<HistoryEntity>)

    @Query("SELECT * FROM history")
    fun getAll(): MutableLiveData<List<HistoryEntity>>

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistory()

}