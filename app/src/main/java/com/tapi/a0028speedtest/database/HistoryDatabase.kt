package com.tapi.a0028speedtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tapi.a0028speedtest.database.dao.HistoryDAO
import com.tapi.a0028speedtest.database.entities.HistoryEntity

const val DATABASE_NAME = "speed_test_db"


@Database(entities = [HistoryEntity::class], exportSchema = false, version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: HistoryDatabase? = null
        fun create(context: Context) {
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        fun get(): HistoryDatabase {
            return instance!!
        }

        private fun buildDatabase(context: Context): HistoryDatabase {
            return Room.databaseBuilder(context, HistoryDatabase::class.java, DATABASE_NAME).build()
        }
    }

    abstract val historyDAO: HistoryDAO

}