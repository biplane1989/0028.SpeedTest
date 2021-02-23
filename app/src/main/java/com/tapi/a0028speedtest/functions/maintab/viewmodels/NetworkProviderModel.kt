package com.tapi.a0028speedtest.functions.maintab.viewmodels

import androidx.lifecycle.*
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkView
import com.tapi.a0028speedtest.functions.maintab.objs.NetworkItem
import com.tapi.nettraffic.objects.NetworkType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class NetworkProviderModel : BaseViewModel() {


    private var _networkData = MediatorLiveData<List<NetWorkView>?>()

    private var _networkPerfect = MutableLiveData<NetWorkView>()
    val networkPerfect: LiveData<NetWorkView> get() = _networkPerfect
    private var filterText = ""

    init {
        _networkData.addSource(mockDataList()) {
            if (it.isNotEmpty()) {
                _networkData.value = it
            }
        }
    }

    private val _FilterNetworkData = liveData<List<NetWorkView>?> {
        emitSource(_networkData.map {
            it?.let {
                var listRs: List<NetWorkView>? = null
                listRs = ArrayList(it)
                if (filterText.isNotEmpty()) {
                    listRs.clear()
                    it.forEach { item ->
                        val rs = item.networkItem.nameNetwork.toLowerCase(Locale.getDefault())
                            .contains(filterText.toLowerCase(Locale.getDefault()))
                        if (rs) {
                            listRs.add(item)
                        }
                    }
                }
                listRs
            }
        })
    }


    fun getListNetwork(): LiveData<List<NetWorkView>?> {
        return _FilterNetworkData
    }


    fun searchNetwork(text: String) {
        filterText = text
        _networkData.postValue(_networkData.value)
    }

    private fun getListAllNetwork(): ArrayList<NetWorkView> {
        return ArrayList(_networkData.value ?: ArrayList())
    }

    private fun getListFilterNetwork(): ArrayList<NetWorkView> {
        return ArrayList(_FilterNetworkData.value ?: ArrayList())
    }

    init {
        mockDataList()
    }

    private fun mockDataList(): LiveData<List<NetWorkView>> {
        val listData = mutableListOf<NetWorkView>()
        val listNetWorks = mutableListOf("VNPT", "VIETTEL", "FPT", "MOBIFONE", "BEELINE")
        val listLocation =
            mutableListOf("HANOI", "SAIGON", "DANANG", "CANTHO", "VINHLONG", "YENBAI")

        for (item in 1..50) {
            listData.add(
                NetWorkView(
                    NetworkItem(
                        listNetWorks[(0..4).random()],
                        listLocation[(0..5).random()],
                        NetworkType._5G
                    ),
                    Random.nextBoolean()
                )
            )
        }

        val networkLiveData = MutableLiveData<List<NetWorkView>>()
        networkLiveData.value = listData
        return networkLiveData

    }

    fun chooseAutoNetwork() {
        val random = (0..getListAllNetwork().size).random()
        if (random == 0) {
            _networkPerfect.value = getListAllNetwork()[random]
        }
        _networkPerfect.value = getListAllNetwork()[random - 1]
    }


}