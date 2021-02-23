package com.tapi.nettraffic

import android.content.Context
import com.tapi.nettraffic.exceptions.MyNetworkException
import com.tapi.nettraffic.objects.MyNetworkInfo
import com.tapi.nettraffic.objects.NetworkMeasureConfig
import com.tapi.nettraffic.objects.PingStatistics
import kotlinx.coroutines.flow.Flow

data class NetworkRate(val percent: Float, val rate: Float) {
    override fun toString(): String {
        return "percent : $percent rate :$rate"
    }
}

interface NetworkMeasure {
    @Throws(MyNetworkException::class)
    suspend fun connect(): PingStatistics

    @Throws(MyNetworkException::class)
    fun testDownloadChannel(): Flow<NetworkRate>

    @Throws(MyNetworkException::class)
    fun testUploadChannel(): Flow<NetworkRate>

    fun getMyNetworkInfo(): MyNetworkInfo
}

class NetworkMeasureFactory {
    companion object {
        fun createNetworkMeasure(
            appContext: Context,
            config: NetworkMeasureConfig
        ): NetworkMeasure {
            return NetworkMeasureImpl(appContext, config)
        }
    }
}