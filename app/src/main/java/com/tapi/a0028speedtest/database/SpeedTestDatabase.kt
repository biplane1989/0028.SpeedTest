package com.tapi.a0028speedtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tapi.a0028speedtest.database.dao.FavoriteDAO
import com.tapi.a0028speedtest.database.dao.HistoryDAO
import com.tapi.a0028speedtest.database.entities.FavoriteEntity
import com.tapi.a0028speedtest.database.entities.HistoryEntity

const val DATABASE_NAME = "speed_test_db"


@Database(entities = [HistoryEntity::class, FavoriteEntity::class], exportSchema = false, version = 1)
abstract class SpeedTestDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: SpeedTestDatabase? = null
        fun create(context: Context) {
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        fun get(): SpeedTestDatabase {
            return instance!!
        }

        private fun buildDatabase(context: Context): SpeedTestDatabase {
            return Room.databaseBuilder(context, SpeedTestDatabase::class.java, DATABASE_NAME).build()
        }
    }

    abstract val historyDAO: HistoryDAO
    abstract val favoriteDAO: FavoriteDAO

}