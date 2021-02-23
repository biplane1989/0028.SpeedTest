package com.example.testretrofill2.server

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "settings")
data class Settings @JvmOverloads constructor(
    @field: Element(name = "servers") var servers: Servers?=null)