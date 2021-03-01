package com.tapi.a0028speedtest.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapi.a0028speedtest.base.BaseActivity
import com.tapi.a0028speedtest.databinding.NetworkProviderDialogBinding
import com.tapi.a0028speedtest.functions.maintab.adapters.NetworkProviderAdapter
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.a0028speedtest.functions.maintab.viewmodels.ExceptionNetwork
import com.tapi.a0028speedtest.functions.maintab.viewmodels.NetworkProviderModel
import com.tapi.a0028speedtest.util.Constances

class NetworkProviderActivity : BaseActivity(), View.OnClickListener, NetworkProviderAdapter.NetworkListener {

    private var _binding: NetworkProviderDialogBinding? = null
    private val binding get() = _binding!!

    private val networkModel: NetworkProviderModel by viewModels()

    private lateinit var networkAdapter: NetworkProviderAdapter

    private val observerListData = Observer<List<NetWorkViewItem>?> {
        it?.let {
            it.forEach {
                Log.d("TAG", "observerListData: $it")
            }
        }
        if (it != null) {
            networkAdapter.submitList(it)
        }
    }

    private val observerExceptionNetwork = Observer<ExceptionNetwork> {excepton ->
        excepton?.let {
            when (it) {
                ExceptionNetwork.SERVER_NOT_REACH -> {
                    Toast.makeText(this, "SERVER_NOT_REACH", Toast.LENGTH_SHORT).show()
                    finish()
                }
                ExceptionNetwork.NOT_CONNECTED -> {
                    Toast.makeText(this, "NOT_CONNECTED", Toast.LENGTH_SHORT).show()
                    finish()
                }
                ExceptionNetwork.DATA_NULL -> {
                    Toast.makeText(this, "DATA_NULL", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }


    }
    private val observerNetworkPerfect = Observer<NetWorkViewItem> {    //Todo  listener bắt từ diloag về Framgnet
        changeNetworkServer(it)
    }

    //Todo hàm nhận sự kiện khi network thay đổi
    fun changeNetworkServer(netWorkViewItem: NetWorkViewItem) {
        val returnIntent = Intent()
        returnIntent.putExtra(Constances.INTENT_KEY_SERVER, netWorkViewItem.server)
        setResult(RESULT_OK, returnIntent)
        finish()
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

            override fun onTextChanged(textchange: CharSequence?, start: Int, before: Int, count: Int) {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = NetworkProviderDialogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        initView()
        initLists()

        binding.viewmodel = networkModel
        binding.lifecycleOwner = this

        networkModel.networkData.observe(this, observerListData)

        networkModel.networkPerfect.observe(this, observerNetworkPerfect)
        networkModel.exceptionNetWork.observe(this, observerExceptionNetwork)
    }

    private fun initLists() {
        networkAdapter = NetworkProviderAdapter(this, this)
        binding.rvNetwork.layoutManager = LinearLayoutManager(this)
        binding.rvNetwork.adapter = networkAdapter

    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivSearch -> {
                showLayoutSearch()
            }
            binding.backDialogTv -> {
                val returnIntent = Intent()
                setResult(RESULT_CANCELED, returnIntent)
                finish()
            }
            binding.ivClose -> {
                binding.edtSearch.setText("")
            }
            binding.ivBack -> {
                hideLayoutSearch(v)
                searchNetwork("")
                binding.edtSearch.setText("")
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

    private fun hideLayoutSearch(v: View) {
        hideKeyboard(v)
        binding.childSearch.visibility = View.INVISIBLE
        binding.childActionbar.visibility = View.VISIBLE
    }


    private fun hideKeyboard(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun showKeyboard() {
        binding.edtSearch.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun changeServer(netWorkViewItem: NetWorkViewItem) {
        if (netWorkViewItem.server.sponsor.isNotEmpty()) {
            changeNetworkServer(netWorkViewItem)         //Todo  listener bắt từ diloag về Framgnet
        }
    }

    override fun setFavorite(netWorkViewItem: NetWorkViewItem) {
        networkModel.setFavorite(netWorkViewItem)
    }


    interface NetworkProviderListener {
        fun changeSevrer(netWorkViewItem: NetWorkViewItem)
    }
}