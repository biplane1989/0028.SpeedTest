package com.tapi.nettraffic.objects

enum class NetworkType {
    _2G,
    _3G,
    _4G,
    _5G,
    WIFI,
    UNKNOWN,
    NO_INTERNET
}

data class MyNetworkInfo(val ip: String, val networkType: NetworkType)
