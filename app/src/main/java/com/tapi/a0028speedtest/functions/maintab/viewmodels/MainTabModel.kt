package com.tapi.a0028speedtest.functions.maintab.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.core.manager.SettingManager
import com.tapi.a0028speedtest.core.settting.SettingManagerImpl
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.functions.home.objects.SpeedTestState
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.a0028speedtest.functions.maintab.networks.FakeNetworkManager
import com.tapi.a0028speedtest.functions.maintab.objs.NETWORK_VIEW_ITEM_FAKE
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem

import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
import com.tapi.a0028speedtest.util.Utils
import com.tapi.nettraffic.NetworkMeasure
import com.tapi.nettraffic.NetworkMeasureFactory
import com.tapi.nettraffic.NetworkRate
import com.tapi.nettraffic.exceptions.MY_LOCAL_NETWORK_INTERRUPTED
import com.tapi.nettraffic.exceptions.MyNetworkException
import com.tapi.nettraffic.exceptions.NO_INTERNET_CODE
import com.tapi.nettraffic.exceptions.SERVER_NOT_REACH
import com.tapi.nettraffic.objects.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.math.roundToInt


fun Float.convertNumber(): Float {
    return if (!this.isNaN()) {
        Log.d("TAG", "convertNumber: $this")
        (this * 100.0).roundToInt() / 100.0f
    } else {
        -1f
    }
}

class SpeedData(

    var dataDownload: DataNetwork = DataNetwork(),
    var dataUpload: DataNetwork = DataNetwork(),
    var speedState: SpeedTestState = SpeedTestState.IDLE,
    var maxTransferRateDownload: Float = 0f,
    var maxTransferRateUpload: Float = 0f,
    var averagePing: Float = 0f,
    var listDataDownload: ArrayList<NetworkRate> = ArrayList(),
    var listDataUpload: ArrayList<NetworkRate> = ArrayList(),
    var maxSpeed: Int = 100,
    var rateUnit: DataRateUnits = DataRateUnits.KBPS

)

class MainTabModel : BaseViewModel() {

    val settingManager: SettingManager = SettingManagerImpl
    private var pingStatics: PingStatistics? = null
    private val TAG: String = MainTabModel::class.java.name

    private var _resultPing = MutableLiveData<String>()
    val resultPing: LiveData<String> = _resultPing


    private var _stateClick = MutableLiveData<ConnectionType>()
    val stateClick: LiveData<ConnectionType> get() = _stateClick

    private var _networkType = MutableLiveData<NetworkType>()
    val networkType: LiveData<NetworkType> get() = _networkType

    private var _speedData = MutableLiveData<SpeedData>()
    val speedData: LiveData<SpeedData> get() = _speedData


    private lateinit var networkMeasure: NetworkMeasure

    var networkItem: NetWorkViewItem = NETWORK_VIEW_ITEM_FAKE

    private var _settingValue = MutableLiveData<Setting>()
    val settingValue: LiveData<Setting> = _settingValue


    private val observerSetting = Observer<Setting> {
        _settingValue.value = it
    }


    private var _resultDownload = Utils.map(speedData, settingValue) { speedData, setting ->

        if (speedData != null && setting != null) {
            convertUnitListRate(speedData.listDataDownload, setting.testingUnits)
        } else {
            -1f
        }
    }
    val resultDownload: LiveData<Float> = _resultDownload

    var _resultUpload = Utils.map(speedData, settingValue) { speedData, setting ->
        if (speedData != null && setting != null) {
            convertUnitListRate(speedData.listDataUpload, setting.testingUnits)
        } else {
            -1f
        }
    }
    val resultUpload: LiveData<Float> = _resultUpload


    init {
        settingManager.getSetting().observeForever(observerSetting)
        _resultPing.value = "0.0"
        _stateClick.value = ConnectionType.SINGLE
    }

    override fun onCleared() {
        super.onCleared()
        settingManager.getSetting().removeObserver(observerSetting)
    }


    fun initNetworkMesure(context: Context) {
        val server = "speedtestkv1a.viettel.vn"
        val uploadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/upload.php"
        val downloadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/random2000x2000.jpg"
        _stateClick.value?.let {
            val networkMeasureConfig =
                NetworkMeasureConfig(server, uploadingUrl, downloadingUrl, it)
            Log.d(TAG, "initNetworkMesure: ${_stateClick.value!!}")

            networkMeasure =
                NetworkMeasureFactory.createNetworkMeasure(context, networkMeasureConfig)
        }

    }


    fun clickTvMulti() {
        if (_stateClick.value == ConnectionType.SINGLE) {
            _stateClick.value = ConnectionType.MULTI
        }
    }

    fun clickTvSingle() {
        if (_stateClick.value == ConnectionType.MULTI) {
            _stateClick.value = ConnectionType.SINGLE
        }
    }

    fun setNetwork(network: NetWorkViewItem) {
        this.networkItem = network
    }


    fun getNetwork(): NetWorkViewItem {
        return networkItem
    }


    private fun checkValidNetworkType(myNetworkInfo: MyNetworkInfo) {
        when (myNetworkInfo.networkType) {
            NetworkType.UNKNOWN -> {
                _networkType.value = NetworkType.UNKNOWN
            }
            NetworkType.NO_INTERNET -> {
                _networkType.value = NetworkType.NO_INTERNET
            }
            NetworkType._2G -> {
                _networkType.value = NetworkType._2G
            }
            NetworkType._3G -> {
                _networkType.value = NetworkType._3G
            }
            NetworkType._4G -> {
                _networkType.value = NetworkType._4G
            }
            NetworkType._5G -> {
                _networkType.value = NetworkType._5G
            }

            else -> {
                _networkType.value = NetworkType.WIFI
            }
        }
    }

    suspend fun connectServer(context: Context): Boolean {

        initNetworkMesure(context)
        val mSpeedData = SpeedData(speedState = _speedData.value?.speedState ?: SpeedTestState.IDLE)

        return try {

            mSpeedData.speedState = SpeedTestState.CONNECTING
            pingStatics = networkMeasure.connect()
            pingStatics?.let {
                _resultPing.value = "${it.timeAvg().convertNumber()}"
                mSpeedData.averagePing = it.timeAvg().convertNumber()
                _speedData.value = mSpeedData
            }
            val result = networkMeasure.getMyNetworkInfo()
            checkValidNetworkType(result)
            _speedData.value = mSpeedData
            true
        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            _speedData.value = mSpeedData
            false
        }

    }


    private fun checkValidNetwork(e: MyNetworkException) {
        when (e.code) {
            NO_INTERNET_CODE -> {
                _speedData.value?.speedState = SpeedTestState.NO_INTERNET
            }
            SERVER_NOT_REACH -> {
                _speedData.value?.speedState = SpeedTestState.NO_INTERNET
            }
            MY_LOCAL_NETWORK_INTERRUPTED -> {
                _speedData.value?.speedState = SpeedTestState.NO_INTERNET
            }
        }
    }


    suspend fun testDownloadChanel(): Boolean {
        var resultDownload: Boolean
        var mSpeedData = SpeedData(speedState = _speedData.value?.speedState ?: SpeedTestState.IDLE)
        try {
            mSpeedData.speedState = SpeedTestState.RUNNINGDOWNLOAD
            _speedData.value = mSpeedData
            networkMeasure.testDownloadChannel()
//            FakeNetworkManager.testDownloadChannel()
                .flowOn(Dispatchers.IO).collect {
                    mSpeedData.rateUnit = _settingValue.value?.testingUnits ?: DataRateUnits.KBPS

                    mSpeedData.maxTransferRateDownload = Math.max(it.rate, mSpeedData.maxTransferRateDownload)


                    mSpeedData.maxSpeed = _settingValue.value?.gaugeScale ?: 100
                    mSpeedData.dataDownload.transferRate = it.rate
                    mSpeedData.dataDownload.percent = it.percent
                    mSpeedData.listDataDownload.add(it)
                    _speedData.value = mSpeedData
                }

            resultDownload = true

        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            resultDownload = false
        } finally {
            _speedData.value = mSpeedData
        }
        return resultDownload
    }


    suspend fun testUploadChanel(): Boolean {
        var resultUpload: Boolean
        var mSpeedData = SpeedData(
            speedState = _speedData.value?.speedState ?: SpeedTestState.IDLE,
            listDataDownload = _speedData.value?.listDataDownload!!
        )

        try {
            mSpeedData.speedState = SpeedTestState.RUNNINGUPLOAD
            _speedData.value = mSpeedData
            networkMeasure.testUploadChannel()
//            FakeNetworkManager.testUploadChannel()
                .flowOn(Dispatchers.IO)
                .collect {
                    mSpeedData.rateUnit = _settingValue.value?.testingUnits ?: DataRateUnits.KBPS
                    mSpeedData.maxSpeed = _settingValue.value?.gaugeScale ?: 100

                    mSpeedData.maxTransferRateUpload = Math.max(it.rate, mSpeedData.maxTransferRateUpload)

                    mSpeedData.dataUpload.transferRate = it.rate
                    mSpeedData.dataUpload.percent = it.percent

                    mSpeedData.listDataUpload.add(it)

                    _speedData.value = mSpeedData
                }
            mSpeedData.speedState = SpeedTestState.DONE

            resultUpload = true

        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            resultUpload = false
        } finally {
            _speedData.value = mSpeedData
        }
        return resultUpload
    }


    private fun convertUnitListRate(
        listDatas: ArrayList<NetworkRate>,
        dataRateUnits: DataRateUnits
    ): Float {

        var sum = 0f
        listDatas.forEach {
            sum += it.rate
        }
        var valueRate = sum / listDatas.size
        return when (dataRateUnits) {
            DataRateUnits.KBPS -> {
                (valueRate / Constance.RATE_KBS).convertNumber()
            }
            DataRateUnits.MbPS -> {
                (valueRate / Constance.RATE_Mbps).convertNumber()
            }
            DataRateUnits.MBPS -> {
                (valueRate / Constance.RATE_MBS).convertNumber()
            }
        }
    }

    fun restart() {

        _resultPing.value = "0.0"
        _resultUpload.value = 0f
        _resultUpload.value = 0f


    }

}