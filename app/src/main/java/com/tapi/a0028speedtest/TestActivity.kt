package com.tapi.a0028speedtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tapi.nettraffic.NetworkMeasure
import com.tapi.nettraffic.objects.DownloadChannelInfo
import com.tapi.nettraffic.objects.PingChannelInfo
import com.tapi.nettraffic.objects.UploadChannelInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive


class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_setting)
    }
}