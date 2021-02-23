package com.tapi.a0028speedtest.functions.maintab.dialogs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.base.BaseDialog
import com.tapi.a0028speedtest.databinding.NetworkProviderDialogBinding
import com.tapi.a0028speedtest.functions.maintab.adapters.NetworkProviderAdapter
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkView
import com.tapi.a0028speedtest.functions.maintab.objs.NetworkItem
import com.tapi.a0028speedtest.functions.maintab.viewmodels.NetworkProviderModel

class NetworkProviderDialog(val event: NetworkProviderListener) : BaseDialog(),
    View.OnClickListener, NetworkProviderAdapter.NetworkListener {

    private var _binding: NetworkProviderDialogBinding? = null
    private val binding get() = _binding!!
    private val networkModel: NetworkProviderModel by viewModels()

    private lateinit var networkAdapter: NetworkProviderAdapter

    private val observerListData = Observer<List<NetWorkView>?> {
        it?.let {
            it.forEach {
                Log.d("TAG", "observerListData: $it")
            }
        }
        if (it != null) {
            networkAdapter.submitList(it)
        }
    }
    private val observerNetworkPerfect = Observer<NetWorkView> {
        event.changeSevrer(it.networkItem)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DefaultDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NetworkProviderDialogBinding.inflate(inflater, container, false)
        initView()
        initLists()
        return binding.root
    }

    private fun initView() {
        changeTextSearch()
        binding.ivSearch.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.automaticTv.setOnClickListener(this)
        binding.backDialogTv.setOnClickListener(this)
    }

    private fun changeTextSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                textchange: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                Log.d("TAG", "onTextChanged: $textchange")
                searchNetwork(textchange.toString())
                if (textchange.toString().isNotEmpty()) {
                    binding.ivClose.visibility = View.VISIBLE
                } else {
                    binding.ivClose.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchNetwork(textchange: String) {
        if (textchange.isEmpty()) {
            networkModel.searchNetwork("")
        }
        networkModel.searchNetwork(textchange)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkModel.getListNetwork().observe(this, observerListData)
        networkModel.networkPerfect.observe(this, observerNetworkPerfect)
    }

    private fun initLists() {
        networkAdapter = NetworkProviderAdapter(requireContext(), this)
        binding.rvNetwork.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNetwork.adapter = networkAdapter

    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivSearch -> {
                showLayoutSearch()
            }
            binding.backDialogTv -> {
                dismiss()
            }
            binding.ivClose -> {
                binding.edtSearch.setText("")
            }
            binding.ivBack -> {
                hideLayoutSearch()
            }
            binding.automaticTv -> {
                networkModel.chooseAutoNetwork()
            }
        }
    }

    private fun showLayoutSearch() {
        showKeyboard()
        binding.childSearch.visibility = View.VISIBLE
        binding.childActionbar.visibility = View.INVISIBLE
    }

    private fun hideLayoutSearch() {
        hideKeyboard()
        binding.childSearch.visibility = View.INVISIBLE
        binding.childActionbar.visibility = View.VISIBLE
    }


    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showKeyboard() {
        binding.edtSearch.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun changeServer(networkItem: NetworkItem) {
        if (networkItem.nameNetwork.isNotEmpty()) {
            event.changeSevrer(networkItem)
        }
    }


    interface NetworkProviderListener {
        fun changeSevrer(networkItem: NetworkItem)
    }
}