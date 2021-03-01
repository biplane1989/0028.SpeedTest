package com.tapi.vpncore.listserver

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(strict = false, name = "servers")
data class Servers  @JvmOverloads constructor(
    @field: ElementList(inline = true)
   var server: List<Server>? = null )
