package com.tapi.a0028speedtest.database.objects

import com.tapi.vpncore.objects.Host

data class HistoryItem(val id: Int, val client: Host, val server: Host, val pingRate: Int, val downloadTrace: List<Float>, val uploadTrace: List<Float>, val externalIP: String, val created: Long)