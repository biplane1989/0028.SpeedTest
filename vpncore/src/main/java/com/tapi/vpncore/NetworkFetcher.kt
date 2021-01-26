package com.tapi.vpncore

import android.content.Context
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.objects.Host

interface NetworkFetcher {
    @Throws(MyNetworkException::class)
    fun getMyNetwork(context: Context): Host

    @Throws(MyNetworkException::class)
    fun getListServers(): List<Host>

    @Throws(MyNetworkException::class)
    fun findBestServer(servers: List<Host>): Host
}