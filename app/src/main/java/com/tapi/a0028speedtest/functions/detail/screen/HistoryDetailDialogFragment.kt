package com.tapi.a0028speedtest.functions.detail.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.database.objects.HistoryItem
import com.tapi.a0028speedtest.databinding.HistoryDetailDialogBinding
import com.tapi.a0028speedtest.functions.detail.viewmodel.HistoryDetailViewModel
import com.tapi.a0028speedtest.functions.detail.dialog.HistoryDetailDeleteDialog
import com.tapi.a0028speedtest.functions.history.dateCreated
import com.tapi.a0028speedtest.functions.history.getdownloadRate
import com.tapi.a0028speedtest.functions.history.getupdateRate
import com.tapi.a0028speedtest.ui.viewscustom.linespeedview.objs.DataNetwork
import com.tapi.a0028speedtest.util.Utils
import kotlinx.coroutines.delay

class HistoryDetailDialogFragment : BaseDialog(), View.OnClickListener, HistoryDetailDeleteDialog.HistoryDetailDialogListener {

    private var _binding: HistoryDetailDialogBinding? = null
    private val binding: HistoryDetailDialogBinding get() = _binding!!

    private val viewModel: HistoryDetailViewModel by viewModels()

    val historyItemObserver = Observer<HistoryItem> {
        binding.dateTv.text = it.dateCreated
        binding.pingTv.text = it.pingRate.toString()
        binding.downloadTv.text = it.getdownloadRate
        binding.uploadTv.text = it.getupdateRate
        binding.connectionsAccountTv.text = it.server.name
        binding.networkProvidersAccountTv.text = it.client.networkProvider
        binding.userAccountTv.text = it.client.name

        binding.latValueTv.text = it.server.lat
        binding.lonValueTv.text = it.server.lon
        binding.internalValueTv.text = it.client.ip
        binding.externalValueTv.text = it.externalIP

        lifecycleScope.launchWhenResumed {
            delay(100)
            var percentDownload = 0
            for (item in it.downloadTrace) {
                percentDownload++
                val mData = DataNetwork()
                mData.transferRate = item
                mData.percent = percentDownload.toFloat()
                binding.lineView.startDrawDownload(mData)
            }
            var percentUpload = 0
            for (item in it.uploadTrace) {
                percentUpload++
                val mData = DataNetwork()
                mData.transferRate = item
                mData.percent = percentUpload.toFloat()
                binding.lineView.startDrawUpload(mData)
            }
        }

    }

    companion object {
        val TAG = "LanguageDialog"
        val BUNDLE_KEY_ID = "BUNDLE_KEY_ID"

        @JvmStatic
        fun newInstance(id: Int): HistoryDetailDialogFragment {
            val dialog = HistoryDetailDialogFragment()
            val bundle = Bundle()
            bundle.putInt(BUNDLE_KEY_ID, id)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HistoryDetailDialogBinding.inflate(inflater, container, false)

        viewModel.historyItem.observe(viewLifecycleOwner, historyItemObserver)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.HistoryDetailDialog)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(requireArguments().getInt(BUNDLE_KEY_ID))

        binding.backButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
        binding.shareButton.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backButton -> {
                dismiss()
            }
            binding.deleteButton -> {
                if (childFragmentManager.findFragmentByTag(HistoryDetailDeleteDialog.TAG) == null) {
                    val dialog = HistoryDetailDeleteDialog.newInstance(this)
                    dialog.show(childFragmentManager, HistoryDetailDeleteDialog.TAG)
                }

            }
            binding.shareButton -> {
                Utils.shareFileAudio(requireContext(), "orange", null)      // share data
            }
            binding.vipButton -> {

            }
        }
    }

    override fun onOKListener() {
        viewModel.deleteItem(requireArguments().getInt(BUNDLE_KEY_ID))
        dismiss()
    }
}