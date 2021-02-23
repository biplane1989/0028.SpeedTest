package com.tapi.a0028speedtest.functions.history.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.tapi.a0028speedtest.base.BaseFragment
import com.tapi.a0028speedtest.databinding.HistoryFragmentBinding
import com.tapi.a0028speedtest.functions.history.HistoryViewModel
import com.tapi.a0028speedtest.functions.history.SortType
import com.tapi.a0028speedtest.functions.history.adapter.HistoryAdapter
import com.tapi.a0028speedtest.functions.history.dialog.HistoryDeleteDialog
import com.tapi.a0028speedtest.functions.history.dialog.HistoryDeleteDialog.HistoryDialogListener
import com.tapi.a0028speedtest.functions.detail.screen.HistoryDetailDialogFragment
import com.tapi.a0028speedtest.util.Utils
import com.tapi.a0028speedtest.data.History

class HistoryFragment : BaseFragment(), View.OnClickListener, HistoryDialogListener {

    private var _binding: HistoryFragmentBinding? = null
    private val binding: HistoryFragmentBinding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter(this::onItemClicked)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecycleView()
        initListener()

        viewModel.sortType.observe(viewLifecycleOwner, this::setSortSelected)

    }

    private fun initListener() {
        binding.deleteButton.setOnClickListener(this)
        binding.shareButton.setOnClickListener(this)
        binding.testStartButton.setOnClickListener(this)
    }

    private fun initRecycleView() {
        binding.recycleView.adapter = adapter

        viewModel.listHistory.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setSortSelected(type: SortType) {
        binding.typeButton.isSelected = type == SortType.TYPE_ASC || type == SortType.TYPE_DES
        binding.dateButton.isSelected = type == SortType.DATE_ASC || type == SortType.DATE_DSC
        binding.sortDownloadButton.isSelected = type == SortType.DOWNLOAD_ASC || type == SortType.DOWNLOAD_DSC
        binding.sortUploadButton.isSelected = type == SortType.UPLOAD_ASC || type == SortType.UPLOAD_DSC
    }

    private fun onItemClicked(item: History) {
        if (childFragmentManager.findFragmentByTag(HistoryDetailDialogFragment.TAG) == null) {
            val dialog = HistoryDetailDialogFragment.newInstance(item.id)
            
            dialog.show(childFragmentManager, HistoryDetailDialogFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.deleteButton -> {
                val dialog = HistoryDeleteDialog.newInstance(this)
                dialog.show(childFragmentManager, HistoryDeleteDialog.TAG)
            }
            binding.shareButton -> {
                Utils.shareFileAudio(requireContext(), "tomato", null)          // share data
            }
            binding.testStartButton -> {
            }
        }
    }

    override fun onOKListener() {
        viewModel.deleteAllItem()
    }

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment {
            val historyFragment = HistoryFragment()
            return historyFragment
        }
    }
}