package com.tapi.a0028speedtest.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.data.NetworkType
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.database.entities.HistoryEntity
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.vpncore.objects.Host
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

fun Host.encode(): String {
    val objectJson = JSONObject()
    objectJson.put("name", name)
    objectJson.put("location", location)
    objectJson.put("lat", lat)
    objectJson.put("lon", lon)
    objectJson.put("ip", ip)
    objectJson.put("networkProvider", networkProvider)

    return objectJson.toString()
}

fun String.decodeToHost(): Host {
    val jsonObject = JSONObject(this)
    return Host(jsonObject.get("name").toString(), jsonObject.get("location")
        .toString(), jsonObject.get("lat").toString(), jsonObject.get("lon")
        .toString(), jsonObject.get("ip").toString(), jsonObject.get("networkProvider").toString())
}

fun String.decodeToListFloat(): List<Float> {
    val jsonArray = JSONArray(this)
    val arrResult = ArrayList<Float>()
    for (i in 0 until jsonArray.length()) {
        arrResult.add(jsonArray.get(i).toString().toFloat())
    }
    return arrResult
}

fun List<Float>.encode(): String {
    val listJson = JSONArray()
    for (item in this) {
        listJson.put(item)
    }
    return listJson.toString()
}

fun History.toEntity(): HistoryEntity {
    return HistoryEntity(this.id, client.encode(), server.encode(), downloadRate, updateRate, this.pingRate, downloadTrace.encode(), uploadTrace.encode(), externalIP, created, networkType.toString(), connectionType.toString())
}

fun HistoryEntity.toHistory(): History {
    val networkType = when (NetworkType.valueOf(this.networkType)) {
        NetworkType.WIFI -> NetworkType.WIFI
        NetworkType._2G -> NetworkType._2G
        NetworkType._3G -> NetworkType._3G
        NetworkType._4G -> NetworkType._4G
        NetworkType._5G -> NetworkType._5G

    }
    val connectionType = when (ConnectionType.valueOf(this.connectionType)) {
        ConnectionType.SINGLE -> ConnectionType.SINGLE
        ConnectionType.MULTI -> ConnectionType.MULTI
    }
    return History(id, client.decodeToHost(), server.decodeToHost(), downloadRate, updateRate, pingRate, downloadTrace.decodeToListFloat(), uploadTrace.decodeToListFloat(), externalIP, created, networkType, connectionType)
}

class DBHelperImpl : DBHelper {

    override suspend fun saveHistory(item: History) {
        HistoryDatabase.get().historyDAO.insert(item.toEntity())
    }

    override suspend fun deleteHistory(id: Int) {
        HistoryDatabase.get().historyDAO.delete(id)
    }

    override suspend fun deleteAllHistory() {
        HistoryDatabase.get().historyDAO.deleteAllHistory()
    }

    override fun getHistoryItems(): LiveData<List<History>> {
        return HistoryDatabase.get().historyDAO.getAll()
            .map { entities ->                                   // map nay la map cua tranfromtion.map
                entities.map { it.toHistory() }                  // map nay la cua collection
            }
    }

    override suspend fun getHistoryItemById(id: Int): History? {
        val historyEntity = HistoryDatabase.get().historyDAO.getHistoryById(id)
        return historyEntity.toHistory()
    }

    override suspend fun saveHistory(item: List<History>) {
        HistoryDatabase.get().historyDAO.insert(item.map { it.toEntity() })
    }
}