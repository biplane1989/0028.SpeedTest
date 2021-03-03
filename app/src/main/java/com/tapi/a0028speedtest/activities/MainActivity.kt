package com.tapi.a0028speedtest.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseActivity
import com.tapi.a0028speedtest.util.Constances

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActionReceived(actionName: String, data: Any?): Boolean {
        if (TextUtils.equals(actionName,Constances.ACTION_UPGRADE_VIP)){
            Toast.makeText(this, "Vip action", Toast.LENGTH_SHORT).show()
        }
        return super.onActionReceived(actionName, data)
    }
}