package com.tapi.nettraffic

class SysExecutor {
    companion object {
        fun runCmd(cmd: String): Process? {
            try {
                return Runtime.getRuntime().exec(cmd)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}