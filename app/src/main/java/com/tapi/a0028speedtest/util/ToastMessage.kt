package com.tapi.a0028speedtest.util

import android.widget.Toast
import com.tapi.a0028speedtest.MyApplication

class ToastMessage {
    companion object{
        fun show(res: Int) {
            Toast.makeText(MyApplication.appContext, res, Toast.LENGTH_SHORT).show()
        }
        fun show(message:String) {
            Toast.makeText(MyApplication.appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}