package com.tapi.a0028speedtest.functions.history

import android.annotation.SuppressLint
import com.tapi.a0028speedtest.database.objects.DataRateUnits
import com.tapi.a0028speedtest.database.objects.HistoryItem
import com.tapi.a0028speedtest.database.objects.getDownloadRate
import com.tapi.a0028speedtest.database.objects.getUpdateRate
import java.text.SimpleDateFormat
import java.util.*

val HistoryItem.getdownloadRate: String
    get() = String.format("%.2f", getDownloadRate(DataRateUnits.MbPS))


val HistoryItem.getupdateRate: String
    get() = String.format("%.2f", getUpdateRate(DataRateUnits.MbPS))


val HistoryItem.dateCreated: String
    get() = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(created)


val HistoryItem.clientName: String
    get() = client.name