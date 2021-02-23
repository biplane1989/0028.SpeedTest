package com.tapi.a0028speedtest.functions.history

import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.History
import com.tapi.a0028speedtest.data.getDownloadRate
import com.tapi.a0028speedtest.data.getUpdateRate
import java.text.SimpleDateFormat
import java.util.*

val History.getdownloadRate: String
    get() = String.format("%.2f", getDownloadRate(DataRateUnits.MbPS))


val History.getupdateRate: String
    get() = String.format("%.2f", getUpdateRate(DataRateUnits.MbPS))


val History.dateCreated: String
    get() = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(created)


val History.clientName: String
    get() = client.name