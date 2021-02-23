package com.tapi.a0028speedtest.functions.maintab.screens

import android.annotation.SuppressLint
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.MainTabScreenBinding
import com.tapi.a0028speedtest.functions.home.objects.SpeedTestState
import com.tapi.a0028speedtest.functions.maintab.dialogs.NetworkProviderDialog
import com.tapi.a0028speedtest.functions.maintab.dialogs.ResultDometerDialog
import com.tapi.a0028speedtest.functions.maintab.objs.NetworkItem
import com.tapi.a0028speedtest.functions.maintab.viewmodels.MainTabModel
import com.tapi.a0028speedtest.functions.maintab.viewmodels.SpeedData
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.views.TypeLineView
import com.tapi.a0028speedtest.util.Utils
import com.tapi.nettraffic.NetUtil
import com.tapi.nettraffic.objects.NetworkType
import kotlinx.coroutines.*
import java.util.*

fun View.runAnimationIn() {
    val anim = AnimationUtils.loadAnimation(this.context, R.anim.alpha_in_anim)
    this.startAnimation(anim)
}

fun View.runAnimationOut() {
    val anim = AnimationUtils.loadAnimation(this.context, R.anim.alpha_out_anim)
    this.startAnimation(anim)
}

class MainTabScreen : BaseFragment(), View.OnClickListener,
    ResultDometerDialog.ResultDialogListener, NetworkProviderDialog.NetworkProviderListener {


    private var startDometerJob: Job? = null
    private lateinit var dialogNetworkProvider: NetworkProviderDialog
    private var toast: Toast? = null
    val TAG = MainTabScreen::class.java.name
    private var _binding: MainTabScreenBinding? = null
    private val binding get() = _binding!!
    private val mainTabModel: MainTabModel by viewModels()
    var currentState = SpeedTestState.IDLE
    lateinit var resultDialog: ResultDometerDialog


    private val observerSpeedData = Observer<SpeedData> {

        currentState = it.speedState
        lifecycleScope.launchWhenResumed {

            if (it.speedState == SpeedTestState.RUNNINGDOWNLOAD) {
                setSpeedometerColor(R.color.colorLineDownload)
                binding.lineChar.startDrawDownload(
                    it.dataDownload.transferRate,
                    it.dataDownload.percent
                )

                val maxTransferRate = it.maxTransferRateDownload
                val speed =
                    Utils.convertValue(
                        0f,
                        maxTransferRate,
                        0f,
                        it.maxSpeedRate.toFloat(),
                        it.dataDownload.transferRate
                    )
                Log.d(
                    TAG,
                    "maxTransferRate     Download:  ${it.maxTransferRateDownload} speed: $speed"
                )
                if (speed > 0) {
                    binding.spRate.speedTo(speed, 500)
                }
            }


            if (it.speedState == SpeedTestState.RUNNINGUPLOAD) {
                if (it.dataUpload.percent > 0f) {
                    val maxTransferRate = it.maxTransferRateUpload
                    val speed = Utils.convertValue(
                        0f,
                        maxTransferRate,
                        0f,
                        it.maxSpeedRate.toFloat(),
                        it.dataUpload.transferRate
                    )
                    if (speed > 0) {
                        binding.spRate.speedTo(speed, 500)
                    }

                    Log.d(
                        TAG,
                        "maxTransferRate     Upload:  ${it.maxTransferRateUpload}  Speed $speed "
                    )
                    setSpeedometerColor(R.color.colorLineUpload)
                    binding.lineChar.startDrawUpload(
                        it.dataUpload.transferRate,
                        it.dataUpload.percent
                    )
                }
            }



            if (it.speedState == SpeedTestState.DONE) {
                binding.spRate.speedTo(0f, 1000)
                delay(2000)

                if (mainTabModel.listDataDownload.size > 0) {
                    resultDialog = ResultDometerDialog().getInstance(
                        mainTabModel.listDataDownload,
                        mainTabModel.listDataUpload,
                        mainTabModel.getPingResult(),
                        mainTabModel.getDownloadResult(),
                        mainTabModel.getUploadResult(),
                        mainTabModel.getNetwork()
                    )
                    resultDialog.setOnCallBack(this@MainTabScreen)
                }
                resultDialog.show(
                    this@MainTabScreen.childFragmentManager,
                    ResultDometerDialog::class.java.name
                )

            }
        }
    }


    private val observernetworkType = Observer<NetworkType> {
        if (it == NetworkType.NO_INTERNET || it == NetworkType.UNKNOWN) {
            hideViewNoInterner()
        } else {
            showViewNoInterner()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainTabScreenBinding.inflate(inflater, container, false)
        mainTabModel.initNetworkMesure(requireContext())
        binding.mainmodel = mainTabModel
        binding.lifecycleOwner = viewLifecycleOwner
        observers()
        initViews()
        registerReceiverNetwork()
        return binding.root
    }

    private fun initViews() {
        setColorWaveView()
        mainTabModel.setNewDensity(binding.lineChar.getDensityUnit())
        binding.circleWaveView.setOnClickListener(this)
        binding.telecomIv.setOnClickListener(this)
        binding.networkProviderTv.setOnClickListener(this)
        binding.closeProcessIv.setOnClickListener(this)
    }


    private fun setColorWaveView() {
        binding.circleWaveView.setColorRound1(
            getColor(R.color.colorYellowStart),
            getColor(R.color.colorYellowEnd)
        )
        binding.circleWaveView.setColorRound2(
            getColor(R.color.colorBlueStart),
            getColor(R.color.colorBlueEnd)
        )
    }

    private fun observers() {
        mainTabModel.speedData.observe(viewLifecycleOwner, observerSpeedData)
        mainTabModel.networkType.observe(viewLifecycleOwner, observernetworkType)

    }

    fun getColor(color: Int): Int {
        return ContextCompat.getColor(
            requireContext(),
            color
        )

    }

    @SuppressLint("ShowToast")
    private fun generateToast(text: String) {
        if (toast != null) {
            toast!!.cancel()
            toast = null
            toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        } else
            if (toast == null) {
                toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
            }
        toast!!.show()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        toast?.cancel()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.circleWaveView -> {
                startDometer()
            }
            /*   binding.networkProviderTv -> {
                   resultDialog = ResultDometerDialog().getInstance(
                       mainTabModel.listDataDownload,
                       mainTabModel.listDataUpload
                   )
                   resultDialog.setOnCallBack(this@MainTabScreen)
                   resultDialog.show(childFragmentManager, ResultDometerDialog::class.java.name)
               }*/

            binding.closeProcessIv -> {
                lifecycleScope.launchWhenResumed {
                    resetAllLayout()
                }
            }
            binding.telecomIv -> {
                dialogNetworkProvider = NetworkProviderDialog(this)
                dialogNetworkProvider.show(
                    childFragmentManager,
                    NetworkProviderDialog::class.java.name
                )
            }
        }
    }

    private suspend fun resetAllLayout() {
        startDometerJob?.cancelAndJoin()
        mainTabModel.restart()
        binding.lineChar.resetLineChar(TypeLineView.ALL)
        showAndHideView(binding.spRate, binding.circleWaveView)
        binding.connectView.visibility = View.INVISIBLE
        binding.spRate.speedTo(0f, 0)
        showAndHideView(binding.childResult, binding.childCt)
        binding.closeProcessIv.visibility = View.GONE
    }


    private fun startDometer() {
        startDometerJob = lifecycleScope.launchWhenResumed {

            showAndHideView(binding.childCt, binding.childResult)
            showAndHideView(binding.circleWaveView, binding.connectView)
            binding.closeProcessIv.visibility = View.VISIBLE

            val isConnect = mainTabModel.connectServer()
            if (isConnect) {
                showAndHideView(binding.connectView, binding.spRate)
                binding.spRate.startAnim()
                binding.lineChar.setLineGap(5)
                if (mainTabModel.testDownloadChanel()) {
                    binding.spRate.speedTo(0f, 1000)
                    delay(1010)

                    if (mainTabModel.testUploadChanel()) {
//
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

    private fun setSpeedometerColor(color: Int) {
        binding.spRate.setSpeedometerColor(
            ContextCompat.getColor(
                requireContext(),
                color
            )
        )
    }


    fun showAndHideView(viewHide: View, viewShow: View) {
        viewHide.runAnimationOut()
        viewHide.visibility = View.INVISIBLE
        viewShow.runAnimationIn()
        viewShow.visibility = View.VISIBLE
    }

    override fun testAgain() {
        lifecycleScope.launchWhenResumed {
            resetAllLayout()
        }
        resultDialog.dismiss()
    }

    override fun changeSevrer(networkItem: NetworkItem) {
        mainTabModel.setNetwork(networkItem)
        dialogNetworkProvider.dismiss()
        binding.telecomRsTv.text = networkItem.nameNetwork
    }


    companion object {
        @JvmStatic
        fun newInstance(): MainTabScreen {
            val mainTabScreen = MainTabScreen()
            return mainTabScreen
        }
    }


    fun hideViewNoInterner() {
        binding.childMaintab.visibility = View.GONE
        binding.childNointernet.visibility = View.VISIBLE
    }

    fun showViewNoInterner() {

        binding.childMaintab.visibility = View.VISIBLE
        binding.childNointernet.visibility = View.GONE
    }


    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                NetUtil.getNetworkType(requireContext())
                if (isNetworkConnected(requireContext())) {

                    when (NetUtil.getNetworkType(requireContext())) {
                        NetworkType._2G -> {
                            Log.d(TAG, "networkProviderResult: 2g")
                            binding.networkProviderResultTv.text = "2G"
                        }
                        NetworkType._3G -> {
                            Log.d(TAG, "networkProviderResult: 3g")
                            binding.networkProviderResultTv.text = "3G"
                        }
                        NetworkType._4G -> {
                            Log.d(TAG, "networkProviderResult: 4g")
                            binding.networkProviderResultTv.text = "4G"
                        }
                        NetworkType._5G -> {
                            Log.d(TAG, "networkProviderResult: 5g")
                            binding.networkProviderResultTv.text = "5G"
                        }
                        NetworkType.WIFI -> {
                            Log.d(TAG, "networkProviderResult: wifi")
                            binding.networkProviderResultTv.text = "Wifi"
                        }
                    }
                    Log.d(TAG, "onReceive: to connect")
                    showViewNoInterner()
                } else {
                    Log.d(TAG, "onReceive: Non connect")
                    lifecycleScope.launchWhenResumed {
                        resetAllLayout()
                    }
                    hideViewNoInterner()
                }
            } catch (e: Exception) {
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

    override fun onDetach() {
        super.onDetach()
        unRegisterReceiverNetWork()
    }

}