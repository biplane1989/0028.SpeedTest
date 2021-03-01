package com.tapi.a0028speedtest.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.maintab.dialogs.NetworkProviderActivity
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.vpncore.NetworkFetcherImpl
import com.tapi.vpncore.exceptions.DATA_NULL
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.exceptions.NOT_CONNECTED
import com.tapi.vpncore.exceptions.SERVER_NOT_REACH
import kotlinx.coroutines.*


class TestActivity : AppCompatActivity(), NetworkProviderActivity.NetworkProviderListener {

    val TAG = "giangtd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_setting)

        lifecycleScope.launch {
            val network = NetworkFetcherImpl()
            try {
//                val host = network.getMyNetwork(this@TestActivity)
//                Log.d(TAG, "onCreate: host: " + host)
//
//                val listServer = network.getListServers()
//                for (item in listServer) {
//                    Log.d(TAG, "onCreate: server: " + item)
//                }
//
//                val bestServer = network.findBestServer(listServer, host)
//                Log.d(TAG, "onCreate: best server : " + bestServer)
//                Log.d(TAG, "onCreate: domain server : " + bestServer.getDomain())
//                Log.d(TAG, "onCreate: port server : " + bestServer.getPort())


//                val  dialogNetworkProvider = NetworkProviderActivity(this@TestActivity)
//                dialogNetworkProvider.show(supportFragmentManager, "nkjan")
            } catch (e: MyNetworkException) {           // host
                when (e.code) {
                    SERVER_NOT_REACH -> {
//                        _exceptionNetwork.value = ExceptionNetwork.SERVER_NOT_REACH
                        Toast.makeText(this@TestActivity, "SERVER_NOT_REACH", Toast.LENGTH_SHORT).show()
                    }
                    NOT_CONNECTED -> {
//                        _exceptionNetwork.value = ExceptionNetwork.NOT_CONNECTED
                        Toast.makeText(this@TestActivity, "NOT_CONNECTED", Toast.LENGTH_SHORT).show()
                    }
                    DATA_NULL -> {
//                        _exceptionNetwork.value = ExceptionNetwork.DATA_NULL
                        Toast.makeText(this@TestActivity, "DATA_NULL", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    override fun changeSevrer(netWorkViewItem: NetWorkViewItem) {

    }

}