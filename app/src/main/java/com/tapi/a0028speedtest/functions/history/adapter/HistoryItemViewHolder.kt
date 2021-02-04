package com.tapi.a0028speedtest.functions.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.database.objects.HistoryItem
import com.tapi.a0028speedtest.database.objects.NetworkType
import com.tapi.a0028speedtest.databinding.HistoryItemBinding
import com.tapi.a0028speedtest.functions.history.clientName
import com.tapi.a0028speedtest.functions.history.dateCreated
import com.tapi.a0028speedtest.functions.history.getdownloadRate
import com.tapi.a0028speedtest.functions.history.getupdateRate
import com.tapi.a0028speedtest.functions.main.objects.ConnectionType

class HistoryItemViewHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HistoryItem, listener: (HistoryItem) -> Unit) {
        binding.date.text = item.dateCreated
        binding.download.text = item.getdownloadRate
        binding.upload.text = item.getupdateRate

        setNetworkIcon(item)

        binding.root.setOnClickListener {
            listener(item)
        }
    }

    private fun setNetworkIcon(item: HistoryItem) {
        when (item.networkType) {
            NetworkType.WIFI -> {
                if (item.connectionType == ConnectionType.MULTI) {
                    binding.type.setImageResource(R.drawable.history_screen_wifi_multi)
                } else {
                    binding.type.setImageResource(R.drawable.history_screen_wifi_single)
                }
            }
            NetworkType._2G -> {
                if (item.connectionType == ConnectionType.MULTI) {
                    binding.type.setImageResource(R.drawable.history_screen_2g_multi)
                } else {
                    binding.type.setImageResource(R.drawable.history_screen_2g_single)
                }
            }
            NetworkType._3G -> {
                if (item.connectionType == ConnectionType.MULTI) {
                    binding.type.setImageResource(R.drawable.history_screen_3g_multi)
                } else {
                    binding.type.setImageResource(R.drawable.history_screen_3g_single)
                }
            }
            NetworkType._4G -> {
                if (item.connectionType == ConnectionType.MULTI) {
                    binding.type.setImageResource(R.drawable.history_screen_4g_multi)
                } else {
                    binding.type.setImageResource(R.drawable.history_screen_4g_single)
                }
            }
            NetworkType._5G -> {
                if (item.connectionType == ConnectionType.MULTI) {
                    binding.type.setImageResource(R.drawable.history_screen_5g_multi)
                } else {
                    binding.type.setImageResource(R.drawable.history_screen_5g_single)
                }
            }
        }
    }


    companion object {
        fun create(parent: ViewGroup): HistoryItemViewHolder {
            return HistoryItemViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
}