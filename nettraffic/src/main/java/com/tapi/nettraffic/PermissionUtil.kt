package com.tapi.nettraffic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtil {
    companion object{
        fun hasReadPhoneState(context: Context):Boolean{
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
    }
}