package com.tapi.a0028speedtest.functions.home.objects

enum class SpeedTestState {
    IDLE,
    CONNECTING,
    RUNNINGDOWNLOAD,
    RUNNINGUPLOAD,
    NO_INTERNET,
    DONE,
    DOWNLOAD_EXCEED,
    UPLOAD_EXCEED
}