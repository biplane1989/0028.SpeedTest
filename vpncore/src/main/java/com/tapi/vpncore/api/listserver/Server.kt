package com.example.testretrofill2.server

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(strict = false, name = "server")
data class Server @JvmOverloads constructor(
    @field: Attribute(name = "url") var url: String = "",
    @field: Attribute(name = "lat") var lat: Double = 0.0,
    @field: Attribute(name = "lon") var lon: Double = 0.0,
    @field: Attribute(name = "name") var name: String = "",
    @field: Attribute(name = "country") var country: String = "",
    @field: Attribute(name = "cc") var cc: String = "",
    @field: Attribute(name = "sponsor") var sponsor: String = "",
    @field: Attribute(name = "id") var id: Long = 0,
    @field: Attribute(name = "host") var host: String = ""

) {
    fun getDomain(): String {
        return host.split(":").get(0)
    }

    fun getPort(): String {
        return host.split(":").get(1)
    }
}
