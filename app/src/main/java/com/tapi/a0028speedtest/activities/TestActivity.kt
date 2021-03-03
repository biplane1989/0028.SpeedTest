package com.tapi.a0028speedtest.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.vpncore.NetworkFetcherImpl
import com.tapi.vpncore.exceptions.DATA_NULL
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.exceptions.NOT_CONNECTED
import com.tapi.vpncore.exceptions.SERVER_NOT_REACH
import kotlinx.coroutines.*


class TestActivity : AppCompatActivity(){

    val TAG = "NM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_test)

    }

}