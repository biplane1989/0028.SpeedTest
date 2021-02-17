package com.tapi.nettraffic

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.tapi.nettraffic.objects.ICMPReply
import com.tapi.nettraffic.objects.ICMP_NOT_REACH_TO_DEST
import com.tapi.nettraffic.objects.NetworkType
import java.io.BufferedReader
import java.io.InputStreamReader

class NetUtil {
    companion object {
        const val TAG = "NetUtil"

        @SuppressLint("MissingPermission")
        fun getNetworkType(appContext: Context): NetworkType {
            val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cm =
                    appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (checkUsingTransportWifi(appContext)) {
                return NetworkType.WIFI
            } else if (checkUsingTransportMobile(appContext)) {
                if (PermissionUtil.hasReadPhoneState(appContext)) {
                    val androidNetworkInfo = cm.activeNetworkInfo

                    val androidNetworkType: Int
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        androidNetworkType = tm.dataNetworkType
                    } else {
                        androidNetworkType = tm.networkType
                    }
                    return when (androidNetworkType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> NetworkType._2G
                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType._3G
                        TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> NetworkType._4G
                        TelephonyManager.NETWORK_TYPE_NR -> NetworkType._5G
                        else -> NetworkType.UNKNOWN
                    }
                }

            }
            return NetworkType.UNKNOWN
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                    return false
                }
            } else {
                try {
                    val activeNetworkInfo = cm.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        return true;
                    }
                } catch (e: Exception) {
                }
            }
            return false
        }

        fun checkUsingTransportWifi(appContext: Context): Boolean {
            if (isNetworkAvailable(appContext)) {
                val cm =
                        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true;
                        }
                    }
                } else {
                    try {
                        val activeNetworkInfo = cm.activeNetworkInfo
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                                return true
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            return false
        }

        fun checkUsingTransportMobile(appContext: Context): Boolean {
            if (isNetworkAvailable(appContext)) {
                val cm =
                        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true
                        }
                    }
                } else {
                    try {
                        val activeNetworkInfo = cm.activeNetworkInfo
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                            if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                                return true
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            return false
        }


        fun ping(ip: String): ICMPReply {
            try {
                val process = SysExecutor.runCmd("ping -c 1 -w 5 $ip")
                process?.let {
                    val inputStream = BufferedReader(InputStreamReader(it.inputStream))
                    val errorStream = BufferedReader(InputStreamReader(it.errorStream))
                    var pingInfo = ""
                    val flag = process.waitFor()
                    if (flag != 0) {
                        if (flag != 1) {
                            while (true) {
                                val readLine = errorStream.readLine() ?: break
                                pingInfo = "$pingInfo$readLine \n"
                            }
                        }
                    } else {
                        var currLineIndex = 0
                        while (true) {
                            val lineStr = inputStream.readLine()

                            if (lineStr == null) {
                                pingInfo = ""
                                break
                            } else if (currLineIndex == 1) {
                                pingInfo = "$lineStr \n"
                                break
                            } else {
                                currLineIndex++
                            }
                        }
                    }
                    it.destroy()
                    if (pingInfo.contains("time=") && pingInfo.contains("ttl=")) {
                        return parseICMPPacket(pingInfo)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "ping: error $e")
            }
            return ICMP_NOT_REACH_TO_DEST
        }

        private fun parseICMPPacket(pingInfoStr: String): ICMPReply {
            val ttl = (pingInfoStr.split("ttl=")[1].split(" ")[0]).toInt() * 1000
            val timeStr = pingInfoStr.split("time=")[1].split(" ")[0]
            val time = timeStr.toFloat()
            return ICMPReply(ttl, time)
        }
    }
}