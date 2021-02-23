package com.tapi.vpncore

import android.content.Context
import android.location.Location
import android.text.TextUtils
import android.util.Log
import com.example.testretrofill2.server.Server
import com.tapi.vpncore.api.NetworkListServer
import com.tapi.vpncore.api.NetworkServer
import com.tapi.vpncore.objects.Host
import com.tapi.vpncore.api.Result
import com.tapi.vpncore.api.server.ServerDistance
import com.tapi.vpncore.exceptions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

class NetworkFetcherImpl : NetworkFetcher {

    override suspend fun getMyNetwork(context: Context): Host {
        val networkService: NetworkServer = NetworkServer.create()
        val result = networkService.getMyNetworkInfo()
        when (result) {
            is Result.Success -> {
                if (result.data != null) {
                    return Host(result.data.org, result.data.country, result.data.lat.toString(), result.data.lon.toString(), result.data.query, result.data.isp)
                } else {
                    throw MyNetworkException(DATA_NULL, "data null")
                }
            }
            is Result.Failure -> {
                throw MyNetworkException(SERVER_NOT_REACH, "not reach")
            }
            is Result.NetworkError -> {
                throw MyNetworkException(NOT_CONNECTED, "no connected")
            }
        }
    }

    override suspend fun getListServers(): List<Server> {
        val networkService: NetworkListServer = NetworkListServer.create()
        val result = networkService.getListServer()
        when (result) {
            is Result.Success -> {
                val listServer = result.data?.servers?.server
                if (listServer != null) {
                    return listServer
                } else {
                    throw MyNetworkException(DATA_NULL, "data null")
                }
            }
            is Result.Failure -> {
                throw MyNetworkException(SERVER_NOT_REACH, "not reach")
            }

            is Result.NetworkError -> {
                throw MyNetworkException(NOT_CONNECTED, "no connected")
            }
        }
    }

    override suspend fun findBestServer(servers: List<Server>, host: Host): Server {
        val listDistance = ArrayList<ServerDistance>()

        val locationHost = Location("host")
        locationHost.latitude = host.lat.toDouble()
        locationHost.longitude = host.lon.toDouble()

        for (item in servers) {
            val locationServer = Location("Server")
            locationServer.latitude = item.lat
            locationServer.longitude = item.lon
            listDistance.add(ServerDistance(locationHost.distanceTo(locationServer), item.url))     // return result = meters
        }

        val serverDistance = findMinValueInList(listDistance)

        for (item in servers) {
            if (TextUtils.equals(serverDistance.url, item.url)) return item
        }
        return servers[0]
    }

    private fun findMinValueInList(list: ArrayList<ServerDistance>): ServerDistance {
        var minDistance = list[0].distance
        for (item in list) {
            Log.d("giangtd", "distance: " + item.distance + "url: " + item.url)
            if (minDistance > item.distance) minDistance = item.distance
        }
        for (item in list) {
            if (minDistance == item.distance) return item
        }
        return list[0]
    }
}