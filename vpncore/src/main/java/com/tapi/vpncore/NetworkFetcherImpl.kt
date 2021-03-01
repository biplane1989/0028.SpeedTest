package com.tapi.vpncore

import android.content.Context
import com.tapi.vpncore.api.NetworkListServer
import com.tapi.vpncore.api.NetworkServer
import com.tapi.vpncore.api.Result
import com.tapi.vpncore.exceptions.DATA_NULL
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.exceptions.NOT_CONNECTED
import com.tapi.vpncore.exceptions.SERVER_NOT_REACH
import com.tapi.vpncore.listserver.Server
import com.tapi.vpncore.objects.Host
import com.tapi.vpncore.objects.latValue
import com.tapi.vpncore.objects.lonValue

class NetworkFetcherImpl() : NetworkFetcher {
    val networkService: NetworkServer = NetworkServer.create()
    val serversService: NetworkListServer = NetworkListServer.create()
    override suspend fun getMyNetwork(context: Context): Host {
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
        val result = serversService.getListServer()
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
        return servers.sortedBy { Util.distance(it.lat, it.lon, host.latValue(), host.lonValue()) }
            .first()
    }
}