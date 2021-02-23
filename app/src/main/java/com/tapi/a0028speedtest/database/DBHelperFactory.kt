package com.tapi.a0028speedtest.database

object DBHelperFactory {

    private val dbHelper = DBHelperImpl()
    fun getDBHelper(): DBHelper {
        return dbHelper
    }
}