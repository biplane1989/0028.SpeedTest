package com.tapi.vpncore

import android.content.Context
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.objects.Host

interface NetworkFetcher {
    @Throws(MyNetworkException::class)
    suspend fun getMyNetwork(context: Context): Host

    @Throws(MyNetworkException::class)
    suspend fun getListServers(): List<Host>

    @Throws(MyNetworkException::class)
    suspend fun findBestServer(servers: List<Host>): Host
}