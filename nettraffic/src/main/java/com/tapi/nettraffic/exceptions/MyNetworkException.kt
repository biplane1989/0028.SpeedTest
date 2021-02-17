package com.tapi.nettraffic.exceptions

import java.lang.Exception

const val NO_INTERNET_CODE = 1
const val SERVER_NOT_REACH = 2
const val MY_LOCAL_NETWORK_INTERRUPTED = 3
const val NOT_CONNECTED = 4

class MyNetworkException(val code: Int, message: String) : Exception(message) {}