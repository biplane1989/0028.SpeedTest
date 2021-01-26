package com.tapi.nettraffic

import com.tapi.nettraffic.exceptions.MyNetworkException
import com.tapi.nettraffic.objects.DownloadChannelInfo
import com.tapi.nettraffic.objects.PingChannelInfo
import com.tapi.nettraffic.objects.UploadChannelInfo
import kotlinx.coroutines.flow.Flow

interface NetworkMeasure {
    @Throws(MyNetworkException::class)
    suspend fun connect()

    @Throws(MyNetworkException::class)
    fun testPingChannel(): Flow<PingChannelInfo>

    @Throws(MyNetworkException::class)
    fun testDownloadChannel(): Flow<DownloadChannelInfo>

    @Throws(MyNetworkException::class)
    fun testUploadChannel(): Flow<UploadChannelInfo>
}