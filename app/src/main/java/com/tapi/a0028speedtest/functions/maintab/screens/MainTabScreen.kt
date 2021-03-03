package com.tapi.a0028speedtest.functions.maintab.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.data.Setting
import com.tapi.a0028speedtest.databinding.MainTabScreenBinding
import com.tapi.a0028speedtest.functions.home.objects.SpeedTestState
import com.tapi.a0028speedtest.activities.NetworkProviderActivity
import com.tapi.a0028speedtest.functions.common.VipDialogFragment
import com.tapi.a0028speedtest.functions.main.objs.Constance
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.a0028speedtest.functions.maintab.objs.ValueRate
import com.tapi.a0028speedtest.functions.maintab.viewmodels.MainTabModel
import com.tapi.a0028speedtest.functions.maintab.viewmodels.SpeedData
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views.TypeLineView
import com.tapi.a0028speedtest.util.Constances
import com.tapi.nettraffic.NetUtil
import com.tapi.nettraffic.NetworkRate
import com.tapi.nettraffic.objects.ConnectionType
import com.tapi.nettraffic.objects.NetworkType
import com.tapi.vpncore.exceptions.MyNetworkException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay


fun View.runAnimationIn() {
    val anim = AnimationUtils.loadAnimation(this.context, R.anim.alpha_in_anim)
    this.startAnimation(anim)
}

fun View.runAnimationOut() {
    val anim = AnimationUtils.loadAnimation(this.context, R.anim.alpha_out_anim)
    this.startAnimation(anim)
}

class MainTabScreen : BaseFragment(), View.OnClickListener,
    NetworkProviderActivity.NetworkProviderListener {

    val NETWORK_PROVIDER_ACTIVITY = 2021

    private var startDometerJob: Job? = null
    private lateinit var dialogNetworkProvider: NetworkProviderActivity
    private var toast: Toast? = null
    val TAG = MainTabScreen::class.java.name
    private var _binding: MainTabScreenBinding? = null
    private val binding get() = _binding!!
    private val mainTabModel: MainTabModel by viewModels()
    private var mSpeedData = SpeedData()
    private lateinit var mNetworkType: NetworkType
    private lateinit var mConnectionType: ConnectionType



    private val observerSpeedData = Observer<SpeedData> {
        mSpeedData = it

        lifecycleScope.launchWhenResumed {
            if (it.speedState == SpeedTestState.RUNNINGDOWNLOAD) {
                setSpeedometerColor(R.color.colorLineDownload)
                binding.lineChar.startDrawDownload(
                    it.dataDownload.transferRate,
                    it.dataDownload.percent
                )

                if (it.speedNetwork > 0) {
                    binding.spRate.speedTo(it.speedNetwork, Constance.DEFAULT_MOVE_DURATION)
                    Log.d(TAG, "speedNetwork: download ${it.speedNetwork}")
                }
            }


            if (it.speedState == SpeedTestState.RUNNINGUPLOAD) {
                if (it.dataUpload.percent > 0f) {
                    if (it.speedNetwork > 0) {
                        binding.spRate.speedTo(it.speedNetwork, Constance.DEFAULT_MOVE_DURATION)
                        Log.d(TAG, "speedNetwork: upload ${it.speedNetwork}")
                    }


                    setSpeedometerColor(R.color.colorLineUpload)
                    binding.lineChar.startDrawUpload(
                        it.dataUpload.transferRate,
                        it.dataUpload.percent
                    )
                }
            }


            if (it.speedState == SpeedTestState.DONE) {
                binding.spRate.speedTo(0f, Constance.MOVE_DURATION_PREV_STATE)
                delay(2000)

                showAndHideView(binding.childMaintab, binding.childResultComplete)

                initResult(
                    it.listDataDownload,
                    it.listDataUpload,
                    mainTabModel.resultPing.value,
                    mainTabModel.getNetwork()
                )

            }
        }
    }


    private fun initResult(
        listDataDownload: ArrayList<NetworkRate>,
        listDataUpload: ArrayList<NetworkRate>,
        rsPing: String?,
        netWork: NetWorkViewItem
    ) {

        binding.lineCharBg.startDrawDownload(listDataDownload)
        binding.lineCharBg.startDrawUpload(listDataUpload)


        binding.pingResultTvBg.text = rsPing

        /* if (networkItem.nameNetwork != "" && networkItem.location != "") {
             binding.networkName.text = networkItem.nameNetwork
             binding.networkLocationTv.text = networkItem.location
         }*/


        binding.againTvBg.setOnClickListener(this)
    }


    private val settingObserver = Observer<Setting> {
        Log.d(TAG, "connectServer  unitStateObserver: ${it}")
        when (it.testingUnits) {
            DataRateUnits.KBPS -> {
                binding.spRate.unit = getString(R.string.unit_kbs)
            }
            DataRateUnits.MBPS -> {
                binding.spRate.unit = getString(R.string.unit_mbs)
            }
            DataRateUnits.MbPS -> {
                binding.spRate.unit = getString(R.string.unit_mbps)
            }
        }
        binding.spRate.maxSpeed = it.gaugeScale.toFloat()
        Log.d(TAG, "startDometer: $it")
    }


    private val observernetworkType = Observer<NetworkType> {
        mNetworkType = it
        if (it == NetworkType.NO_INTERNET || it == NetworkType.UNKNOWN) {
            hideViewNoInterner()
        } else {
            showViewInternet()
        }
    }

    private val stateClick = Observer<ConnectionType> {
        mConnectionType = it
    }

    private val observerResultDownload = Observer<ValueRate> {
        if (mSpeedData.speedState == SpeedTestState.RUNNINGUPLOAD) {
            binding.downloadResultTv.text = "${it.valueConvert}"
        }
        binding.downloadResultTvBg.text = "${it.valueConvert}"
    }

    private val observerResultUpload = Observer<ValueRate> {
        if (mSpeedData.speedState == SpeedTestState.DONE) {
            binding.uploadResultTv.text = "${it.valueConvert}"
        }
        binding.uploadResultTvBg.text = "${it.valueConvert}"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainTabScreenBinding.inflate(inflater, container, false)

        binding.mainmodel = mainTabModel
        binding.lifecycleOwner = viewLifecycleOwner
        observers()
        initViews()
        registerReceiverNetwork()
        return binding.root
    }

    private fun initViews() {
        setColorWaveView()
        binding.circleWaveView.setOnClickListener(this)
        binding.telecomIv.setOnClickListener(this)
        binding.networkProviderTv.setOnClickListener(this)
        binding.closeProcessIv.setOnClickListener(this)
        binding.againTvBg.setOnClickListener(this)
        binding.vip.setOnClickListener(this)
    }


    private fun setColorWaveView() {
        binding.circleWaveView.setColorRound1(getColor(R.color.colorYellowStart), getColor(R.color.colorYellowEnd))
        binding.circleWaveView.setColorRound2(getColor(R.color.colorBlueStart), getColor(R.color.colorBlueEnd))
    }

    private fun observers() {
        mainTabModel.speedData.observe(viewLifecycleOwner, observerSpeedData)
        mainTabModel.networkType.observe(viewLifecycleOwner, observernetworkType)
        mainTabModel.settingValue.observe(viewLifecycleOwner, settingObserver)
        mainTabModel.stateClick.observe(viewLifecycleOwner, stateClick)
        mainTabModel.resultDownload.observe(viewLifecycleOwner, observerResultDownload)
        mainTabModel.resultUpload.observe(viewLifecycleOwner, observerResultUpload)

    }

    fun getColor(color: Int): Int {
        return ContextCompat.getColor(requireContext(), color)

    }


    @SuppressLint("ShowToast")
    private fun generateToast(text: String) {
        if (toast != null) {
            toast!!.cancel()
            toast = null
            toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        } else if (toast == null) {
            toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        }
        toast!!.show()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        toast?.cancel()
        unRegisterReceiverNetWork()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.circleWaveView -> {
                startDometer()
            }
            binding.againTvBg -> {
                showAndHideView(binding.childResultComplete, binding.childMaintab)
                lifecycleScope.launchWhenResumed {
                    resetAllLayout()
                }
            }

            binding.closeProcessIv -> {
                if (binding.childResultComplete.isVisible) {
                    showAndHideView(binding.childResultComplete, binding.childMaintab)
                }
                lifecycleScope.launchWhenResumed {
                    resetAllLayout()
                }

            }
            binding.telecomIv -> {
                val intent = Intent(requireContext(), NetworkProviderActivity::class.java)
                startActivityForResult(intent, NETWORK_PROVIDER_ACTIVITY)
            }
            binding.vip ->{
                if (childFragmentManager.findFragmentByTag(VipDialogFragment.TAG) == null) {
                    val dialog = VipDialogFragment()
                    dialog.show(childFragmentManager, VipDialogFragment.TAG)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NETWORK_PROVIDER_ACTIVITY) {


            /*   if (resultCode == Activity.RESULT_OK) {
                   server = data?.getParcelableExtra(Constances.INTENT_KEY_SERVER)
                   Log.d(TAG, "onActivityResult: name: 1 " + server?.sponsor)
                   server?.let {
                       mainTabModel.setNetwork(NetWorkViewItem(it, NetUtil.getNetworkType(requireContext()), false, ""))     // Todo gia tri favorite, distance, isSelect dang fake
                       binding.telecomRsTv.text = server?.country
                       binding.telecomTv.text = server?.sponsor
                       Log.d(TAG, "onActivityResult: url: "+server?.getDownloadUrl()?:"" )
                       mainTabModel.createNewNetworkMeasure(requireContext(),server?.getDomain()?:"",server?.getDownloadUrl()?:"",server?.url?:"")
                   }
               }
               if (resultCode == Activity.RESULT_CANCELED) {
                   //todo
               }*/
        }
    }

    private suspend fun resetAllLayout() {
        startDometerJob?.cancelAndJoin()
        mainTabModel.restart()
        binding.lineChar.resetLineChar(TypeLineView.ALL)
        binding.lineCharBg.resetLineChar(TypeLineView.ALL)
        showAndHideView(binding.spRate, binding.circleWaveView)
        binding.connectView.visibility = View.INVISIBLE
        binding.spRate.speedTo(0f, 0)
        showAndHideView(binding.childResult, binding.childCt)
        binding.closeProcessIv.visibility = View.GONE
        binding.downloadResultTv.text = getString(R.string.default_value_rate)
        binding.uploadResultTv.text = getString(R.string.default_value_rate)
        binding.circleWaveView.isEnabled = true
    }



    private fun startDometer() {
        startDometerJob = lifecycleScope.launchWhenResumed {

            binding.circleWaveView.isEnabled = false
            showAndHideView(binding.circleWaveView, binding.connectView)
            showAndHideView(binding.childCt, binding.childResult)

            binding.closeProcessIv.visibility = View.VISIBLE

            val isConnect = mainTabModel.connectServer(requireContext())
            delay(1500)

            if (isConnect) {
                showAndHideView(binding.connectView, binding.spRate)
                binding.spRate.startAnim()
                if (mainTabModel.testDownloadChanel()) {

                    binding.spRate.speedTo(0f, 1000)
                    delay(1010)

                    if (mainTabModel.testUploadChanel()) {

                    } else {
                        //exception can't test upload
                    }
                } else {
                    //exception can't test download
                }
            } else {
                //exception can't connect server
            }
        }
    }

    /*suspend fun saveHistoryToDB() {
        server?.let {
            val host = NetworkFetcherImpl().getMyNetwork(requireContext())
            Log.d(TAG, "onActivityResult: name: 2 " + it.sponsor+ " download : "+ mCurrentDownloadRate + " upate: "+ mCurrentUploadRate+" ping: "+ mSpeedData.averagePing)

            val history = History(
                0,
                host,
                it,
                mCurrentDownloadRate,
                mCurrentUploadRate,
                mSpeedData.averagePing,
                mSpeedData.listDataDownload,
                mSpeedData.listDataUpload,
                host.networkProvider,
                System.currentTimeMillis(),
                convertNetworkType(NetUtil.getNetworkType(requireContext())),
                mConnectionType
            )

            DBHelperFactory.getDBHelper().saveHistory(history)
        }
    }*/

    private fun convertNetworkType(net: NetworkType): com.tapi.a0028speedtest.data.NetworkType {
        return when (net) {
            NetworkType._2G -> com.tapi.a0028speedtest.data.NetworkType._2G
            NetworkType._3G -> com.tapi.a0028speedtest.data.NetworkType._3G
            NetworkType._4G -> com.tapi.a0028speedtest.data.NetworkType._4G
            NetworkType._5G -> com.tapi.a0028speedtest.data.NetworkType._5G
            NetworkType.WIFI -> com.tapi.a0028speedtest.data.NetworkType.WIFI
            else -> com.tapi.a0028speedtest.data.NetworkType.WIFI
        }
    }

    private fun setSpeedometerColor(color: Int) {
        binding.spRate.setSpeedometerColor(ContextCompat.getColor(requireContext(), color))
    }


    fun showAndHideView(viewHide: View, viewShow: View) {
        viewHide.runAnimationOut()
        viewHide.visibility = View.INVISIBLE
        viewShow.runAnimationIn()
        viewShow.visibility = View.VISIBLE
    }


    override fun changeSevrer(netWorkViewItem: NetWorkViewItem) {
        mainTabModel.setNetwork(netWorkViewItem)
//        dialogNetworkProvider.dismiss()
        binding.telecomRsTv.text = netWorkViewItem.server.sponsor
        binding.telecomTv.text = netWorkViewItem.server.country
    }


    companion object {
        @JvmStatic
        fun newInstance(): MainTabScreen {
            val mainTabScreen = MainTabScreen()
            return mainTabScreen
        }
    }

    private fun hideViewNoInterner() {

        binding.progressView.visibility = View.VISIBLE
        binding.telecomRsTv.visibility = View.INVISIBLE
        binding.telecomIv.isEnabled = false
    }

    private fun showViewNoInterner() {
        lifecycleScope.launchWhenResumed {
            resetAllLayout()
        }
        binding.progressView.visibility = View.INVISIBLE
        binding.telecomRsTv.visibility = View.VISIBLE
        binding.telecomIv.isEnabled = true
    }

    private fun showViewInternet() {
        binding.progressView.visibility = View.INVISIBLE
        binding.telecomRsTv.visibility = View.VISIBLE
        binding.telecomIv.isEnabled = true
    }


    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                NetUtil.getNetworkType(requireContext())
                if (isNetworkConnected(requireContext())) {
                    when (NetUtil.getNetworkType(requireContext())) {
                        NetworkType._2G -> {
                            Log.d(TAG, "networkProviderResult: 2g")
                            binding.networkProviderResultTv.text =
                                getString(R.string.maintab_value_2g)
                        }
                        NetworkType._3G -> {
                            Log.d(TAG, "networkProviderResult: 3g")
                            binding.networkProviderResultTv.text =
                                getString(R.string.maintab_value_3g)
                        }
                        NetworkType._4G -> {
                            Log.d(TAG, "networkProviderResult: 4g")
                            binding.networkProviderResultTv.text =
                                getString(R.string.maintab_value_4g)
                        }
                        NetworkType._5G -> {
                            Log.d(TAG, "networkProviderResult: 5g")
                            binding.networkProviderResultTv.text =
                                getString(R.string.maintab_value_5g)
                        }
                        NetworkType.WIFI -> {
                            Log.d(TAG, "networkProviderResult: wifi")
                            binding.networkProviderResultTv.text =
                                getString(R.string.maintab_value_wifi)
                        }
                    }
                    Log.d(TAG, "onReceive: to connect")
                    showViewNoInterner()
                } else {
                    Log.d(TAG, "onReceive: Non connect")
                    hideViewNoInterner()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: MyNetworkException) {
                e.printStackTrace()
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun registerReceiverNetwork() {
        Log.d(TAG, "onReceive: register")
        requireActivity().registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

    }

    private fun unRegisterReceiverNetWork() {
        Log.d(TAG, "onReceive: unregister")
        try {
            requireActivity().unregisterReceiver(networkChangeReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}