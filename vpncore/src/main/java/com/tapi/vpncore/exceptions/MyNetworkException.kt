package com.tapi.vpncore.exceptions

import java.lang.Exception
const val NO_INTERNET_CODE = 1
class MyNetworkException(val code: Int, message: String) : Exception(message) {}