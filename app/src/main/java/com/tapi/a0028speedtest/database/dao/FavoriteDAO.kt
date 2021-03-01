package com.tapi.a0028speedtest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tapi.a0028speedtest.database.entities.FavoriteEntity
import com.tapi.a0028speedtest.database.entities.HistoryEntity

@Dao
interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: List<FavoriteEntity>)

    @Query("SELECT * FROM favorite")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("DELETE FROM favorite WHERE ip = :ip")
    suspend fun delete(ip: String)

    @Query("DELETE FROM favorite")
    suspend fun deleteAllFavorite()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(favoriteEntity: FavoriteEntity)
}