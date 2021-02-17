package com.tapi.nettraffic

import android.content.Context
import com.tapi.nettraffic.exceptions.MyNetworkException
import com.tapi.nettraffic.objects.MyNetworkInfo
import com.tapi.nettraffic.objects.NetworkMeasureConfig
import com.tapi.nettraffic.objects.PingStatistics
import kotlinx.coroutines.flow.Flow

interface NetworkMeasure {
    @Throws(MyNetworkException::class)
    suspend fun connect(): PingStatistics

    @Throws(MyNetworkException::class)
    fun testDownloadChannel(): Flow<Float>

    @Throws(MyNetworkException::class)
    fun testUploadChannel(): Flow<Float>

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