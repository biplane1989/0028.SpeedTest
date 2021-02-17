package com.tapi.nettraffic.objects

const val DEFAULT_PING_TIMES = 4
const val DEFAULT_SIZE_OF_FILE_TO_UPLOAD = 1000000
const val DEFAULT_REPEAT_TIMES_TO_MULTI_MODE = 2
data class NetworkMeasureConfig(
    val server: String,
    val uploadingUrl: String,
    val downloadingUrl: String,
    val connectionType: ConnectionType,
    val pingTimes: Int = DEFAULT_PING_TIMES,
    val sizeOfFileToUpload: Int = DEFAULT_SIZE_OF_FILE_TO_UPLOAD
)