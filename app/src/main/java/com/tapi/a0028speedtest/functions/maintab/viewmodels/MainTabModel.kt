package com.tapi.a0028speedtest.functions.maintab.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.a0028speedtest.base.BaseViewModel
import com.tapi.a0028speedtest.base.SingleLiveEvent
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.functions.home.objects.SpeedTestState
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.a0028speedtest.functions.maintab.networks.FakeNetworkManager
import com.tapi.a0028speedtest.functions.maintab.objs.NetworkItem
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
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


class SpeedData(
    var dataDownload: DataNetwork,
    var dataUpload: DataNetwork,
    var speedState: SpeedTestState = SpeedTestState.IDLE,
    var maxTransferRateDownload: Float = 0f,
    var maxTransferRateUpload: Float = 0f,
    var averageDownload: Float = 0f,
    var averageUpload: Float = 0f,
    var listDataDownload: ArrayList<NetworkRate> = ArrayList(),
    var listDataUpload: ArrayList<NetworkRate> = ArrayList(), var maxSpeedRate: Int = 100

)

class MainTabModel : BaseViewModel() {


    private var pingStatics: PingStatistics? = null
    private val TAG: String = MainTabModel::class.java.name
    private var _onNetworkProviderClicked = SingleLiveEvent<Boolean>()

    private var _unitState = MutableLiveData<DataRateUnits>()
    val unitState: LiveData<DataRateUnits> get() = _unitState

    var _resultPing = MutableLiveData<String>()
    var _rateSpeed = MutableLiveData<Float>()
    var _resultDownload = MutableLiveData<String>()
    var _resultUpload = MutableLiveData<String>()

    private var _stateClick = MutableLiveData<ConnectionType>()
    val stateClick: LiveData<ConnectionType> get() = _stateClick

    private var _networkType = MutableLiveData<NetworkType>()
    val networkType: LiveData<NetworkType> get() = _networkType

    private var _speedData = MutableLiveData<SpeedData>()
    val speedData: LiveData<SpeedData> get() = _speedData

    val listDataDownload = ArrayList<NetworkRate>()
    val listDataUpload = ArrayList<NetworkRate>()
    private lateinit var networkMeasure: NetworkMeasure
    private var mSpeedData = SpeedData(DataNetwork(), DataNetwork(), SpeedTestState.IDLE)

    var maxTransferRateDownload = 0f
    var maxTransferRateUpload = 0f

    var transferRateDownload = 0f
    var transferRateUpload = 0f


    var networkItem: NetworkItem = NetworkItem("", "", NetworkType._4G)
    var _density = Constance.DEFAULT_DENSITY_VALUE

    private var _connectionType = ConnectionType.SINGLE
    var connectionType: ConnectionType
        get() = _connectionType
        set(value) = setValueConnectionType(value)


    private var density: Int
        get() = _density
        set(value) = setNewDensity(value)


    init {
        _resultPing.value = "0.0"
        _resultDownload.value = "0.0"
        _resultUpload.value = "0.0"
        _unitState.value = DataRateUnits.KBPS
        _stateClick.value = ConnectionType.SINGLE
        _rateSpeed.value = 100f

    }



    fun initNetworkMesure(context: Context) {

        val server = "speedtestkv1a.viettel.vn"
        val uploadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/upload.php"
        val downloadingUrl = "http://speedtestkv1a.viettel.vn:8080/speedtest/random2000x2000.jpg"
        val networkMeasureConfig =
            NetworkMeasureConfig(server, uploadingUrl, downloadingUrl, connectionType)
        Log.d(TAG, "initNetworkMesure: $connectionType")

        networkMeasure =
            NetworkMeasureFactory.createNetworkMeasure(context, networkMeasureConfig)
    }

    fun setNewDensity(value: Int) {
        _density = value
    }


    private fun setValueConnectionType(value: ConnectionType) {
        _connectionType = value
    }


    fun changeParameter() {
        _speedData.value = mSpeedData
    }

    fun clickTvMulti() {
        if (_stateClick.value == ConnectionType.SINGLE) {
            _stateClick.value = ConnectionType.MULTI
            setValueConnectionType(ConnectionType.MULTI)
        }
    }

    fun clickTvSingle() {
        if (_stateClick.value == ConnectionType.MULTI) {
            _stateClick.value = ConnectionType.SINGLE
            setValueConnectionType(ConnectionType.SINGLE)
        }
    }

    fun setNetwork(network: NetworkItem) {
        this.networkItem = network
    }


    fun getNetwork(): NetworkItem {
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

    suspend fun connectServer(): Boolean {
        return try {
            mSpeedData.speedState = SpeedTestState.CONNECTING
            pingStatics = networkMeasure.connect()
            setPingResult()
            val result = networkMeasure.getMyNetworkInfo()
            checkValidNetworkType(result)
            changeParameter()
            true
        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            changeParameter()
            false
        }
    }

    fun setPingResult() {
        pingStatics?.let {
            _resultPing.value = "${it.timeAvg().toInt()}"
        }
    }

    fun getPingResult(): String? {
        return _resultPing.value
    }

    fun getDownloadResult(): String? {
        return _resultDownload.value
    }

    fun getUploadResult(): String? {
        return _resultUpload.value
    }

    private fun checkValidNetwork(e: MyNetworkException) {
        when (e.code) {
            NO_INTERNET_CODE -> {
                mSpeedData.speedState = SpeedTestState.NO_INTERNET
            }
            SERVER_NOT_REACH -> {
                mSpeedData.speedState = SpeedTestState.NO_INTERNET
            }
            MY_LOCAL_NETWORK_INTERRUPTED -> {
                mSpeedData.speedState = SpeedTestState.NO_INTERNET
            }
        }
    }

    suspend fun testUploadChanel(): Boolean {
        var resultUpload = false
        try {
            mSpeedData.speedState = SpeedTestState.RUNNINGUPLOAD
            changeParameter()
            networkMeasure.testUploadChannel()
            //FakeNetworkManager.testUploadChannel()
                .flowOn(Dispatchers.IO)
                .collect {
                    val transferRate = it.rate
                    maxTransferRateUpload = setMaxTransferRate(transferRate)
                    mSpeedData.dataUpload.transferRate = transferRate
                    mSpeedData.dataUpload.percent = it.percent
                    Log.d(TAG, "NetworkChanel  testUploadChanel:   - percent :$it")
                    listDataUpload.add(it)
                    transferRateUpload = transferRate
                    mSpeedData.maxTransferRateUpload = maxTransferRateUpload
                    changeParameter()

                }
            mSpeedData.speedState = SpeedTestState.DONE


            var sumRate = 0f
            listDataUpload.forEach {
                sumRate += it.rate
            }
            val valueRate = convertRateNetwork(sumRate)

            _resultUpload.value = "$valueRate"
            resultUpload = true

        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            resultUpload = false
        } finally {
            changeParameter()
        }
        return resultUpload
    }


    suspend fun testDownloadChanel(): Boolean {
        var resultDownload = false
        try {
            mSpeedData.speedState = SpeedTestState.RUNNINGDOWNLOAD
            changeParameter()
            networkMeasure.testDownloadChannel()
            //FakeNetworkManager.testDownloadChannel()
                .flowOn(Dispatchers.IO).collect {
                    val transferRate = it.rate

                    maxTransferRateDownload = setMaxTransferRate(transferRate)
                    mSpeedData.dataDownload.transferRate = transferRate
                    mSpeedData.dataDownload.percent = it.percent

                    Log.d(TAG, " NetworkChanel  testDownloadChanel:  - percent $it")
                    listDataDownload.add(it)
                    transferRateDownload = transferRate
                    mSpeedData.maxTransferRateDownload = maxTransferRateDownload
                    changeParameter()
                }


            var sumRate = 0f
            listDataDownload.forEach {
                sumRate += it.rate
            }
            val valueRate = convertRateNetwork(sumRate)


            _resultDownload.value = "$valueRate"

            resultDownload = true

        } catch (e: MyNetworkException) {
            checkValidNetwork(e)
            resultDownload = false
        } finally {
            changeParameter()
        }
        Log.d(TAG, "startDometer:  resultDownload $resultDownload")
        return resultDownload
    }

    private fun convertRateNetwork(sumRate: Float): Int {
        return when (_unitState.value) {
            DataRateUnits.KBPS -> {
                ((sumRate / listDataDownload.size) * Constance.RATE_KBS).toInt()
            }
            DataRateUnits.MbPS -> {
                ((sumRate / listDataDownload.size) * Constance.RATE_Mbps).toInt()
            }
            DataRateUnits.MBPS -> {
                ((sumRate / listDataDownload.size) * Constance.RATE_MBS).toInt()
            }
            else -> {
                -1
            }
        }
    }


    private fun setMaxTransferRate(tmp: Float): Float {
        if (maxTransferRateDownload < tmp) {
            return tmp
        }
        return maxTransferRateDownload
    }

    fun restart() {
        mSpeedData.dataDownload = DataNetwork(0f, 0f)
        mSpeedData.dataUpload = DataNetwork(0f, 0f)
        mSpeedData.speedState = SpeedTestState.IDLE
        mSpeedData.maxTransferRateDownload = 0f
        mSpeedData.maxTransferRateUpload = 0f
        mSpeedData.averageDownload = 0f
        mSpeedData.averageUpload = 0f
        _resultPing.value = "0.0"
        _resultDownload.value = "0.0"
        _resultUpload.value = "0.0"
        listDataUpload.clear()
        listDataDownload.clear()
        mSpeedData.listDataUpload.clear()
        mSpeedData.listDataDownload.clear()
        maxTransferRateDownload = 0f
        maxTransferRateUpload = 0f
        transferRateDownload = 0f
        transferRateUpload = 0f

        _density = Constance.DEFAULT_DENSITY_VALUE
        changeParameter()
    }

}
