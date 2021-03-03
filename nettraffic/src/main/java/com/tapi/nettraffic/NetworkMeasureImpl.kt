package com.tapi.nettraffic

import android.content.Context
import android.util.Log
import com.tapi.nettraffic.api.NetworkService
import com.tapi.nettraffic.api.Result
import com.tapi.nettraffic.core.SpeedTestReport
import com.tapi.nettraffic.core.SpeedTestSocket
import com.tapi.nettraffic.core.inter.ISpeedTestListener
import com.tapi.nettraffic.core.model.SpeedTestError
import com.tapi.nettraffic.exceptions.MyNetworkException
import com.tapi.nettraffic.exceptions.NOT_CONNECTED
import com.tapi.nettraffic.exceptions.NO_INTERNET_CODE
import com.tapi.nettraffic.exceptions.SERVER_NOT_REACH
import com.tapi.nettraffic.objects.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

internal class NetworkMeasureImpl(val appContext: Context, val config: NetworkMeasureConfig) :
    NetworkMeasure {
    private var mMyNetworkInfo: MyNetworkInfo = MyNetworkInfo("", NetworkType.UNKNOWN)

    override suspend fun connect(): PingStatistics {
        mMyNetworkInfo = MyNetworkInfo("", NetworkType.UNKNOWN)
        if (NetUtil.isNetworkAvailable(appContext)) {
            val networkType = NetUtil.getNetworkType(appContext)
            val networkService: NetworkService = NetworkService.create()
            val result = networkService.getMyNetworkInfo()
            val ip = when (result) {
                is Result.Success -> result.data?.query ?: ""
                is Result.Failure -> ""
                Result.NetworkError -> ""
            }

            mMyNetworkInfo = MyNetworkInfo(ip, networkType)
            if (ip.isNotEmpty()) {
                // start ping to server
                val packets = ArrayList<ICMPReply>()
                for (i in 1..config.pingTimes) {
                    packets.add(NetUtil.ping(config.server))
                }
               return  PingStatistics(packets)
            }

        } else {
            mMyNetworkInfo = MyNetworkInfo("", NetworkType.NO_INTERNET)
        }
        return PingStatistics(makeICMPNotReachToDest(config.pingTimes))

    }
    inline fun <reified E : Any> ProducerScope<E>.offerSafe(element: E) {
        if (isActive) {
            offer(element)
        }
    }

    @ExperimentalCoroutinesApi
    override fun testDownloadChannel(): Flow<NetworkRate> {
        if (NetUtil.isNetworkAvailable(appContext) && mMyNetworkInfo.ip.isNotEmpty()) {
            return callbackFlow {
                val speedTestSocket = SpeedTestSocket()
                var countCompletedTimes = 0
                speedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
                    override fun onCompletion(report: SpeedTestReport) {
                        Log.d("TAG", "onCompletion:   ${report.transferRateBit} ")
                        countCompletedTimes++
                        if (config.connectionType == ConnectionType.SINGLE) {
                            offerSafe(NetworkRate(1f, report.transferRateBit.toFloat()))
                            close()
                        } else {
                            if (countCompletedTimes == DEFAULT_REPEAT_TIMES_TO_MULTI_MODE) {
                                close()
                            } else {
                                speedTestSocket.startDownload(config.downloadingUrl)
                            }
                        }
                    }

                    override fun onProgress(percent: Float, report: SpeedTestReport) {
                        if (config.connectionType == ConnectionType.SINGLE) {
                            offerSafe(
                                NetworkRate(
                                    percent / 100f,
                                    report.transferRateBit.toFloat()
                                )
                            )
                        } else {
                            offerSafe(
                                NetworkRate(
                                    (countCompletedTimes + percent / 100f) / DEFAULT_REPEAT_TIMES_TO_MULTI_MODE,
                                    report.transferRateBit.toFloat()
                                )
                            )
                        }
                    }

                    override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                        if (speedTestError == SpeedTestError.SOCKET_TIMEOUT) {
                            close(MyNetworkException(SERVER_NOT_REACH, errorMessage))
                        } else {
                            close(MyNetworkException(NOT_CONNECTED, errorMessage))
                        }

                    }
                })

                speedTestSocket.startDownload(config.downloadingUrl)

                awaitClose()
            }

        } else {
            throw MyNetworkException(
                NO_INTERNET_CODE,
                "test download channel failed cause no internet"
            )
        }

    }

    @ExperimentalCoroutinesApi
    override fun testUploadChannel(): Flow<NetworkRate> {
        if (NetUtil.isNetworkAvailable(appContext) && mMyNetworkInfo.ip.isNotEmpty()) {
            return callbackFlow {
                val speedTestSocket = SpeedTestSocket()
                var countCompletedTimes = 0
                speedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
                    override fun onCompletion(report: SpeedTestReport) {

                        Log.d("TAG", "onCompletion: testUploadChannel  ${report.transferRateBit} ")

                        countCompletedTimes++
                        if (config.connectionType == ConnectionType.SINGLE) {
                            offerSafe(NetworkRate(1f, report.transferRateBit.toFloat()))
                            close()
                        } else {
                            if (countCompletedTimes == DEFAULT_REPEAT_TIMES_TO_MULTI_MODE) {
                                close()
                            } else {
                                speedTestSocket.startUpload(
                                    config.uploadingUrl,
                                    config.sizeOfFileToUpload
                                )
                            }
                        }
                    }

                    override fun onProgress(percent: Float, report: SpeedTestReport) {
                        if (config.connectionType == ConnectionType.SINGLE) {
                            offerSafe(
                                NetworkRate(
                                    percent / 100f,
                                    report.transferRateBit.toFloat()
                                )
                            )
                        } else {
                            offerSafe(
                                NetworkRate(
                                    (countCompletedTimes + percent / 100f) / DEFAULT_REPEAT_TIMES_TO_MULTI_MODE,
                                    report.transferRateBit.toFloat()
                                )
                            )
                        }
                    }

                    override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                        if (speedTestError == SpeedTestError.SOCKET_TIMEOUT) {
                            close(MyNetworkException(SERVER_NOT_REACH, errorMessage))
                        } else {
                            close(MyNetworkException(NOT_CONNECTED, errorMessage))
                        }
                    }
                })
                speedTestSocket.startUpload(config.uploadingUrl, config.sizeOfFileToUpload)
                awaitClose()
            }

        } else {
            throw MyNetworkException(
                NO_INTERNET_CODE,
                "test download channel failed cause no internet"
            )
        }
    }

    override fun getMyNetworkInfo(): MyNetworkInfo {
        return mMyNetworkInfo
    }
}