package com.tapi.a0028speedtest.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.data.NetworkType
import com.tapi.a0028speedtest.database.entities.FavoriteEntity
import com.tapi.a0028speedtest.database.entities.HistoryEntity
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.vpncore.listserver.Server
import com.tapi.vpncore.objects.Host
import org.json.JSONArray
import org.json.JSONObject
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


fun Server.encode(): String {
    val objectJson = JSONObject()
    objectJson.put("url", url)
    objectJson.put("name", name)
    objectJson.put("lat", lat)
    objectJson.put("lon", lon)
    objectJson.put("country", country)
    objectJson.put("cc", cc)
    objectJson.put("sponsor", sponsor)
    objectJson.put("id", id)
    objectJson.put("host", host)
    return objectJson.toString()
}

fun String.decodeToServer(): Server {
    val jsonObject = JSONObject(this)
    return Server(
        jsonObject.getString("url"),
        jsonObject.getDouble("lat"),
        jsonObject.getDouble("lon"),
        jsonObject.getString("name"),
        jsonObject.getString("country"),
        jsonObject.getString("cc"),
        jsonObject.getString("sponsor"),
        jsonObject.getLong("id"),
        jsonObject.getString("host")
    )
}


fun String.decodeToHost(): Host {
    val jsonObject = JSONObject(this)
    return Host(
        jsonObject.getString("name").toString(), jsonObject.getString("location"),
        jsonObject.getString("lat"), jsonObject.getString("lon")
        , jsonObject.getString("ip"), jsonObject.getString("networkProvider")
    )
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
    return HistoryEntity(
        this.id,
        client.encode(),
        server.encode(),
        downloadRate,
        updateRate,
        this.pingRate,
        downloadTrace.encode(),
        uploadTrace.encode(),
        externalIP,
        created,
        networkType.toString(),
        connectionType.toString()
    )
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
    return History(
        id,
        client.decodeToHost(),
        server.decodeToServer(),
        downloadRate,
        updateRate,
        pingRate,
        downloadTrace.decodeToListFloat(),
        uploadTrace.decodeToListFloat(),
        externalIP,
        created,
        networkType,
        connectionType
    )
}

class DBHelperImpl : DBHelper {

    override suspend fun saveHistory(item: History) {
        SpeedTestDatabase.get().historyDAO.insert(item.toEntity())
    }

    override suspend fun deleteHistory(id: Int) {
        SpeedTestDatabase.get().historyDAO.delete(id)
    }

    override suspend fun deleteAllHistory() {
        SpeedTestDatabase.get().historyDAO.deleteAllHistory()
    }

    override fun getHistoryItems(): LiveData<List<History>> {
        return SpeedTestDatabase.get().historyDAO.getAll()
            .map { entities ->                                   // map nay la map cua tranfromtion.map
                entities.map { it.toHistory() }                  // map nay la cua collection
            }
    }

    override suspend fun getHistoryItemById(id: Int): History? {
        val historyEntity = SpeedTestDatabase.get().historyDAO.getHistoryById(id)
        return historyEntity.toHistory()
    }

    override suspend fun saveHistory(item: List<History>) {
        SpeedTestDatabase.get().historyDAO.insert(item.map { it.toEntity() })
    }

    override suspend fun saveFavorite(item: FavoriteEntity) {
        SpeedTestDatabase.get().favoriteDAO.insert(item)
    }

    override suspend fun deleteFavorite(ip: String) {
        SpeedTestDatabase.get().favoriteDAO.delete(ip)
    }

    override suspend fun getAllFavorite(): List<FavoriteEntity> {
        return SpeedTestDatabase.get().favoriteDAO.getAll()
    }

    override suspend fun updateFavorite(item: FavoriteEntity) {
        SpeedTestDatabase.get().favoriteDAO.update(item)
    }
}