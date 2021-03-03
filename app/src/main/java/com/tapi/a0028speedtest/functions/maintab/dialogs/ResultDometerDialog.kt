package com.tapi.a0028speedtest.functions.maintab.dialogs


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.data.DataRateUnits
import com.tapi.a0028speedtest.databinding.ResultMaintabDialogBinding
import com.tapi.a0028speedtest.functions.maintab.objs.NETWORK_VIEW_ITEM_FAKE
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.nettraffic.NetworkRate

class ResultDometerDialog() : BaseDialog(), View.OnClickListener {


    private var _binding: ResultMaintabDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var mCallback: ResultDialogListener
    private lateinit var listDataNetworksDownload: List<NetworkRate>
    private lateinit var listDataNetworksUpload: List<NetworkRate>
    private var networkItem: NetWorkViewItem = NETWORK_VIEW_ITEM_FAKE
    private var pingRs: String? = null
    private var downloadRs: Float? = null
    private var uploadRS:Float? = null
    private lateinit var rateUnit: DataRateUnits


    fun getInstance(
        listDataDownload: ArrayList<NetworkRate>,
        listDataUpload: ArrayList<NetworkRate>,
        pingRs: String?,
        downloadRs: Float?,
        uploadRs: Float?,
        networkItem: NetWorkViewItem, rateUnits: DataRateUnits
    ): ResultDometerDialog {
        val dialog = ResultDometerDialog()
        dialog.listDataNetworksDownload = ArrayList(listDataDownload)
        dialog.listDataNetworksUpload = ArrayList(listDataUpload)
        dialog.networkItem = networkItem
        dialog.pingRs = pingRs
        dialog.uploadRS = uploadRs
        dialog.downloadRs = downloadRs
        dialog.rateUnit = rateUnits
        return dialog
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)
    }

    fun setOnCallBack(event: ResultDialogListener) {
        mCallback = event
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResultMaintabDialogBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init() {



                binding.lineCharBg.startDrawDownload(listDataNetworksDownload)
                binding.lineCharBg.startDrawUpload(listDataNetworksUpload)



        pingRs?.let {
            binding.pingResultTvBg.text = it
        }
        downloadRs?.let {
            binding.downloadResultTvBg.text = "$it"
        }
        uploadRS?.let {
            binding.uploadResultTvBg.text = "$it"
        }

        /*if (networkItem.nameNetwork != "" && networkItem.location != "") {
            binding.networkName.text = networkItem.nameNetwork
            binding.networkLocationTv.text = networkItem.location
        }*/

        when(rateUnit){
            DataRateUnits.KBPS->{
               setUnitToText(getString(R.string.unit_kbs))
            }
            DataRateUnits.MbPS->{
                setUnitToText(getString(R.string.unit_mbps))

            }
            DataRateUnits.MBPS->{
                setUnitToText(getString(R.string.unit_mbs))
            }
        }

        binding.againTvBg.setOnClickListener(this)
    }

    private fun setUnitToText(unit:String) {
        binding.unitUploadTv.text = unit
        binding.unitDownloadTv.text = unit
    }


    override fun onClick(v: View) {
        when (v) {
            binding.againTvBg -> {
                binding.lineCharBg.setDensityScreen(200)
                mCallback.testAgain()
            }
        }
    }



    private fun fakeList(): List<NetworkRate> {

        var listData = ArrayList<NetworkRate>()
        for (i in 1..1000) {
            listData.add(NetworkRate(i / 1000f, (i * 5..i * 10).random().toFloat()))
        }
        listData.add(NetworkRate(1f, (1000 * 5..1000 * 10).random().toFloat()))
        return listData
    }

    private fun fakeListUpload(): List<NetworkRate> {

        var listData = ArrayList<NetworkRate>()
        for (i in 1..200) {
            listData.add(NetworkRate(i / 200f, (i * 5..i * 10).random().toFloat()))
        }
        listData.add(NetworkRate(1f, (200 * 5..200 * 10).random().toFloat()))
        return listData
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mCallback.testAgain()
    }



    interface ResultDialogListener {
        fun testAgain()
    }

}