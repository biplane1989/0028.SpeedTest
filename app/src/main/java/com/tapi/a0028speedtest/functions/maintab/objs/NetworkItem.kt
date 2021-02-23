package com.tapi.a0028speedtest.functions.maintab.objs

import com.tapi.nettraffic.objects.NetworkType

data class NetworkItem(
    val nameNetwork: String,
    val location: String, val networkType: NetworkType
) {
    override fun toString(): String {
        return "nameNetwork :$nameNetwork  location $location"
    }
}

class NetWorkView(val networkItem: NetworkItem, var favorite: Boolean)
