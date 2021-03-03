package com.tapi.a0028speedtest.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tapi.a0028speedtest.data.NetworkType
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.vpncore.objects.Host

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "client") var client: String,
    @ColumnInfo(name = "server") var server: String,
    @ColumnInfo(name = "downloadRate") var downloadRate: Float,
    @ColumnInfo(name = "updateRate") var updateRate: Float,
    @ColumnInfo(name = "pingRate") var pingRate: Float,
    @ColumnInfo(name = "downloadTrace") var downloadTrace: String,
    @ColumnInfo(name = "uploadTrace") var uploadTrace: String,
    @ColumnInfo(name = "externalIP") var externalIP: String,
    @ColumnInfo(name = "created") var created: Long,
    @ColumnInfo(name = "networkType") var networkType: String,
    @ColumnInfo(name = "connectionType") var connectionType: String
)
