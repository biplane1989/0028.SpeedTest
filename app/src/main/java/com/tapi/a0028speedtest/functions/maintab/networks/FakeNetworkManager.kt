package com.tapi.a0028speedtest.functions.maintab.networks

import com.tapi.nettraffic.NetworkMeasure
import com.tapi.nettraffic.NetworkRate
import com.tapi.nettraffic.objects.MyNetworkInfo
import com.tapi.nettraffic.objects.PingStatistics
import com.tapi.nettraffic.objects.makeICMPNotReachToDest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

object FakeNetworkManager : NetworkMeasure {
    override suspend fun connect(): PingStatistics {
        delay(1000)
        return PingStatistics(makeICMPNotReachToDest(5))
    }


    override fun testDownloadChannel(): Flow<NetworkRate> = flow {
        for (i in 1 until 201) {
            delay(20)
            emit(NetworkRate(percent = i / 200f, rate = (i * 500..i * 1000).random().toFloat()))

        }
        emit(NetworkRate(percent = 1f, rate = (200 * 500..200 * 1000).random().toFloat()))
    }

    override fun testUploadChannel(): Flow<NetworkRate> = flow {
        for (i in 1 until 201) {
            delay(20)
            emit(NetworkRate(percent = i / 200f, rate = (i * 500..i * 1000).random().toFloat()))

        }
        emit(NetworkRate(percent = 1f, rate = (200 * 500..200 * 1000).random().toFloat()))
    }

    override fun getMyNetworkInfo(): MyNetworkInfo {
        TODO("Not yet implemented")
    }
}

