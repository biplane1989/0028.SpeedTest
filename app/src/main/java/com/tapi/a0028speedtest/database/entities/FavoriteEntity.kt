package com.tapi.a0028speedtest.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavoriteEntity(
    @PrimaryKey()
    @ColumnInfo(name = "ip") var ip: String="",
    @ColumnInfo(name = "server") var server: String="")