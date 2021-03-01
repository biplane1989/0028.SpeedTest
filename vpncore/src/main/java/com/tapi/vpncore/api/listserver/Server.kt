package com.tapi.vpncore.listserver

import android.os.Parcel
import android.os.Parcelable
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

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString()) {
    }

    fun getDomain(): String {
        return host.split(":").get(0)
    }

    fun getPort(): String {
        return host.split(":").get(1)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeString(cc)
        parcel.writeString(sponsor)
        parcel.writeLong(id)
        parcel.writeString(host)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Server> {
        override fun createFromParcel(parcel: Parcel): Server {
            return Server(parcel)
        }

        override fun newArray(size: Int): Array<Server?> {
            return arrayOfNulls(size)
        }
    }
}
