package com.tapi.a0028speedtest.functions.maintab.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.core.manager.SettingManager
import com.tapi.a0028speedtest.core.settting.SettingManagerImpl
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.functions.home.objects.SpeedTestState
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.a0028speedtest.functions.maintab.objs.NETWORK_VIEW_ITEM_FAKE
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.a0028speedtest.functions.maintab.objs.ValueRate
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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
    var speedNetwork: Float = 0f,
    var averagePing: Float = 0f,
    var listDataDownload: ArrayList<NetworkRate> = ArrayList(),
    var listDataUpload: ArrayList<NetworkRate> = ArrayList()

)

class MainTabModel : BaseViewModel() {

    private val settingManager: SettingManager = SettingManagerImpl
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

    val settingValue: LiveData<Setting> = settingManager.getSetting()




    private var _resultDownload = Utils.map(speedData, settingValue) { speedData, setting ->

        if (speedData != null && setting != null) {
            convertListRate(speedData.listDataDownload, setting.testingUnits)
        } else {
            ValueRate(0f, 0f)
        }
    }
    val resultDownload: LiveData<ValueRate> = _resultDownload

    var _resultUpload = Utils.map(speedData, settingValue) { speedData, setting ->
        if (speedData != null && setting != null) {
            convertListRate(speedData.listDataUpload, setting.testingUnits)
        } else {
            ValueRate(0f, 0f)
        }
    }
    val resultUpload: LiveData<ValueRate> = _resultUpload

    private var _server = "speedtest2.phmbb.net"
    val server get() = _server


    private var _uploadingUrl = "http://speedtest2.phmbb.net:8080/speedtest/upload.php"
    val uploadUrl get() = _uploadingUrl


    var _downloadingUrl = "http://speedtest2.phmbb.net:8080/speedtest/random2000x2000.jpg"
    val downloadUrl get() = _downloadingUrl


    init {
        _resultPing.value = "0.0"
        _stateClick.value = ConnectionType.SINGLE
    }



    private fun initNetworkMesure(
        context: Context
    ) {
        _stateClick.value?.let {
            Log.d(
                TAG,
                "initNetworkMesure: uploadUrl $uploadUrl  " +
                        "downloadUrl $downloadUrl  server $server"
            )
            val networkMeasureConfig =
                NetworkMeasureConfig(server, uploadUrl, downloadUrl, it)

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
            withContext(Dispatchers.Default){
                pingStatics = networkMeasure.connect()
            }
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
                .catch {
                    Log.d(TAG, "testDownloadChannel: ${this.toString()}")
                }
                .flowOn(Dispatchers.IO).collect {

                    mSpeedData.speedNetwork = convertRateNetwork(it.rate)
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
            listDataDownload = _speedData.value?.listDataDownload ?: ArrayList()
        )

        try {
            mSpeedData.speedState = SpeedTestState.RUNNINGUPLOAD
            _speedData.value = mSpeedData
            networkMeasure.testUploadChannel()
//            FakeNetworkManager.testUploadChannel()
                .catch {
                    Log.d(TAG, "testUploadChanel: ${this.toString()}")
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    mSpeedData.speedNetwork = convertRateNetwork(it.rate)
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

    private fun convertListRate(
        listDatas: ArrayList<NetworkRate>,
        dataRateUnits: DataRateUnits
    ): ValueRate {
        var sum = 0f
        listDatas.forEach {
            sum += it.rate
        }
        var valueRate = sum / listDatas.size
        return when (dataRateUnits) {
            DataRateUnits.KBPS -> {
                ValueRate((valueRate / Constance.RATE_KBS).convertNumber(), valueRate)
            }
            DataRateUnits.MbPS -> {
                ValueRate((valueRate / Constance.RATE_Mbps).convertNumber(), valueRate)
            }
            DataRateUnits.MBPS -> {
                ValueRate((valueRate / Constance.RATE_MBS).convertNumber(), valueRate)
            }
        }
    }

    private fun convertRateNetwork(
        valueRate: Float
    ): Float {

        return when (settingValue.value?.testingUnits ?: DataRateUnits.KBPS) {
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


    fun createNewNetworkMeasure(
        context: Context,
        server: String,
        urlDownload: String,
        urlUpload: String
    ) {
        _server = server
        _downloadingUrl = urlDownload
        _uploadingUrl = urlUpload
        initNetworkMesure(context)
    }

    fun restart() {
        _speedData.value = SpeedData(speedState = SpeedTestState.IDLE)
        _resultPing.value = "0.0"
        _resultUpload.value = ValueRate(0f, 0f)
        _resultUpload.value = ValueRate(0f, 0f)

    }

}

