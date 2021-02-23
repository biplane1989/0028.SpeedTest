package com.tapi.a0028speedtest.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.nettraffic.NetworkMeasure
import kotlinx.coroutines.flow.Flow
import com.tapi.vpncore.NetworkFetcher
import com.tapi.vpncore.NetworkFetcherImpl
import kotlinx.coroutines.*


class TestActivity : AppCompatActivity() {

    val TAG = "giangtd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_setting)

        lifecycleScope.launch {
            val network = NetworkFetcherImpl()

            val host = network.getMyNetwork(this@TestActivity)
            Log.d(TAG, "onCreate: host: " + host)

            val listServer = network.getListServers()
            for (item in listServer) {
                Log.d(TAG, "onCreate: server: " + item)
            }

            val bestServer = network.findBestServer(listServer, host)
            Log.d(TAG, "onCreate: best server : " + bestServer)
            Log.d(TAG, "onCreate: domain server : " + bestServer.getDomain())
            Log.d(TAG, "onCreate: port server : " + bestServer.getPort())
        }

    }
}