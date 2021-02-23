package com.tapi.nettraffic

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tapi.nettraffic.objects.NetworkMeasureConfig
import com.tapi.nettraffic.objects.NetworkType
import com.tapi.nettraffic.objects.countPacketReceived
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NetworkMeasureAndroidTest {
    @Test
    fun testGetMyNetworkByUseWifiConnection() = runBlocking {

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val server = "speedtestkv1a.viettel.vn"
        val uploadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/upload.php"
        val downloadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/random2000x2000.jpg"

        val networkMeasureConfig =
                NetworkMeasureConfig(server, uploadingUrl, downloadingUrl, ConnectionType.SINGLE)
        val networkMeasure =
                NetworkMeasureFactory.createNetworkMeasure(appContext, networkMeasureConfig)
        val pingStatistic = networkMeasure.connect()
        val myNetworkInfo = networkMeasure.getMyNetworkInfo()
        Assert.assertEquals(NetworkType.WIFI, myNetworkInfo.networkType)
        Assert.assertNotEquals("", myNetworkInfo.ip)
        Assert.assertEquals(networkMeasureConfig.pingTimes, pingStatistic.countPacketReceived())
    }
    @Test
    fun testDownoadChannel() = runBlocking {

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val server = "speedtestkv1a.viettel.vn"
        val uploadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/upload.php"
        val downloadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/random2000x2000.jpg"

        val networkMeasureConfig =
                NetworkMeasureConfig(server, uploadingUrl, downloadingUrl, ConnectionType.MULTI)
        val networkMeasure =
                NetworkMeasureFactory.createNetworkMeasure(appContext, networkMeasureConfig)
        val pingStatistic = networkMeasure.connect()
        networkMeasure.testUploadChannel()
            .catch {
                Log.d("taihhhhh", "exception")
            }
            .collect {
            Log.d("taihhhhh", "percent ${it.percent} bit rate: ${it.rate}")
        }
    }

}