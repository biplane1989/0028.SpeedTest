package com.tapi.a0028speedtest.functions.maintab.viewmodels

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.*
import com.tapi.a0028speedtest.base.BaseAndroidViewModel
import com.tapi.a0028speedtest.database.DBHelperFactory
import com.tapi.a0028speedtest.database.entities.FavoriteEntity
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.nettraffic.NetUtil
import com.tapi.vpncore.NetworkFetcherImpl
import com.tapi.vpncore.Util
import com.tapi.vpncore.exceptions.DATA_NULL
import com.tapi.vpncore.exceptions.MyNetworkException
import com.tapi.vpncore.exceptions.NOT_CONNECTED
import com.tapi.vpncore.exceptions.SERVER_NOT_REACH
import com.tapi.vpncore.listserver.Server
import com.tapi.vpncore.objects.Host
import com.tapi.vpncore.objects.latValue
import com.tapi.vpncore.objects.lonValue
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

enum class ExceptionNetwork {
    SERVER_NOT_REACH, NOT_CONNECTED, DATA_NULL
}

class NetworkProviderModel(application: Application) : BaseAndroidViewModel(application) {

    private val mContext = getApplication<Application>().applicationContext
    private var _networkData = MediatorLiveData<List<NetWorkViewItem>>()
    val networkData: LiveData<List<NetWorkViewItem>> get() = _networkData

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> get() = _loading

    private val _exceptionNetwork = MutableLiveData<ExceptionNetwork>()
    val exceptionNetWork: LiveData<ExceptionNetwork> get() = _exceptionNetwork

    private var _networkPerfect = MutableLiveData<NetWorkViewItem>()
    val networkPerfect: LiveData<NetWorkViewItem> get() = _networkPerfect

    private val _searchServer = MutableLiveData<String>()

    private var filterText = ""
    val network = NetworkFetcherImpl()
    lateinit var host: Host
    lateinit var listServer: List<Server>
    var listNetworkServer = ArrayList<NetWorkViewItem>()

    val empty = Transformations.map(_networkData) {
        it == null || it.isEmpty()
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    val networkType = NetUtil.getNetworkType(mContext)
                    host = network.getMyNetwork(mContext)
                    listServer = network.getListServers()
                    var listData = ArrayList<NetWorkViewItem>()
                    for (item in listServer) {
                        listData.add(NetWorkViewItem(item, networkType, false, getDistance(Util.distance(host.latValue(), host.lonValue(), item.lat, item.lon)
                            .toInt())))
                    }

                    listData = ArrayList(checkFavorite(listData))
                    listData.sortByDescending { it.favorite }
                    listNetworkServer = listData


                    if (isActive) {
                        withContext(Dispatchers.Main) {
                            _networkData.value = listData
                            _loading.value = false
                        }
                    }
                } catch (e: MyNetworkException) {           // host
                    when (e.code) {
                        SERVER_NOT_REACH -> {
                            _exceptionNetwork.value = ExceptionNetwork.SERVER_NOT_REACH
                        }
                        NOT_CONNECTED -> {
                            _exceptionNetwork.value = ExceptionNetwork.NOT_CONNECTED
                        }
                        DATA_NULL -> {
                            _exceptionNetwork.value = ExceptionNetwork.DATA_NULL
                        }
                    }
                }
            }
        }

        _networkData.addSource(_searchServer) {
            viewModelScope.launch {
                val searchValue = _searchServer.value
                if (searchValue == null) return@launch
                var listData = listNetworkServer.sortedByDescending { it.favorite }
                if (!listData.isNullOrEmpty()) {
                    if (searchValue.isNotEmpty()) {
                        listData = listData.filter {
                            it.server.sponsor.toLowerCase(Locale.getDefault())
                                .contains(searchValue.toLowerCase(Locale.getDefault()))
                        } as ArrayList<NetWorkViewItem>
                    }
                    listData = ArrayList(checkFavorite(listData))
                    listData.sortedByDescending { it.favorite }

                    if (isActive) {
                        withContext(Dispatchers.Main) {
                            _networkData.value = listData
                        }
                    }
                }
            }
        }
    }

    private fun getDistance(distance: Int): String {
        val result = distance / 1000
        return if (result >= 1) {
            "$result km"
        } else {
            "$distance m"
        }
    }

    fun setFavorite(netWorkViewItem: NetWorkViewItem) {
        viewModelScope.launch {
            val newFavorite = getFavoriteEntityByHost(host)
            _networkData.value?.let {
                var index = 0
                val listCopy = ArrayList(it)
                for (item in listCopy) {
                    if (TextUtils.equals(netWorkViewItem.server.url, item.server.url)) {
                        val newItem = item.copy()
                        if (netWorkViewItem.server.url != "") {         // chưa có host
                            if (newFavorite.ip == "") {
                                DBHelperFactory.getDBHelper()
                                    .saveFavorite(FavoriteEntity(host.ip, netWorkViewItem.server.url))
                                newItem.favorite = true
                            } else {
                                if (netWorkViewItem.server.url != newFavorite.server) {         // server được chọn là server mới
                                    DBHelperFactory.getDBHelper()
                                        .updateFavorite(FavoriteEntity(host.ip, netWorkViewItem.server.url))
                                    newItem.favorite = true
                                } else {                                                    // hủy favorite của server đã được chọn
                                    DBHelperFactory.getDBHelper()
                                        .updateFavorite(FavoriteEntity(host.ip, ""))
                                    newItem.favorite = false
                                }
                            }
                        }
                        listCopy.set(index, newItem)
                    } else {
                        val newItem: NetWorkViewItem = item.copy()
                        newItem.favorite = false
                        listCopy.set(index, newItem)
                    }
                    index++
                }

                _networkData.value = listCopy
                listNetworkServer = listCopy
            }
        }
    }

    private suspend fun checkFavorite(listData: List<NetWorkViewItem>): List<NetWorkViewItem> {
        val newFavorite = getFavoriteEntityByHost(host)
        if (newFavorite.server != "") {
            for (item in listData) {
                item.favorite = TextUtils.equals(newFavorite.server, item.server.url)
            }
        }
        return listData
    }

    private suspend fun getFavoriteEntityByHost(host: Host): FavoriteEntity {
        val listFavorite = DBHelperFactory.getDBHelper().getAllFavorite()
        val favorite = listFavorite.filter { it.ip == host.ip }
        var newFavorite = FavoriteEntity()
        if (!favorite.isEmpty()) {
            newFavorite = favorite[0]
        }
        return newFavorite
    }

    fun searchNetwork(text: String) {
        filterText = text
        _searchServer.value = text
    }

    fun chooseAutoNetwork() {
        viewModelScope.launch {
            if (!listNetworkServer.isNullOrEmpty()) {
                val server = network.findBestServer(listServer, host)
                _networkData.value?.let {
                    val listData = ArrayList(it)
                    for (item in listData) {
                        if (server.url == item.server.url) {
                            _networkPerfect.value = item
                            break
                        }
                    }
                }
            }
        }
    }
}