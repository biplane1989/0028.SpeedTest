package com.tapi.a0028speedtest.functions.maintab.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkView
import com.tapi.a0028speedtest.functions.maintab.objs.NetworkItem

class NetworkProviderAdapter(val mContext: Context, val event: NetworkListener) :
        ListAdapter<NetWorkView, NetworkProviderAdapter.NetworkProviderHolder>(NetworkDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkProviderHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_network, parent, false)
        return NetworkProviderHolder(view)
    }

    override fun onBindViewHolder(holder: NetworkProviderHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: NetworkProviderHolder, position: Int) {
        val item = getItem(position)
        when (item.favorite) {
            true -> {
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorLineUpload))
            }
            false -> {
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }
        }
        holder.tvName.text = item.networkItem.nameNetwork
        holder.tvLocation.text = item.networkItem.location


    }


    inner class NetworkProviderHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.name_network_providers_item_tv)
        var tvLocation: TextView = itemView.findViewById(R.id.location_network_item_tv)
        var ivChoose: ImageView = itemView.findViewById(R.id.iv_choose)

        init {
            ivChoose.setOnClickListener(this)
            itemView.setOnClickListener {
                event.changeServer(getItem(adapterPosition).networkItem)
            }
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.iv_choose -> {
                    showMenuChoose()
                }
            }
        }

        private fun showMenuChoose() {
            val wrapper = ContextThemeWrapper(mContext, R.style.PopupMenu)
            PopupMenu(wrapper, ivChoose).apply {
                inflate(R.menu.network_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.choose_server -> {
                            event.changeServer(getItem(adapterPosition).networkItem)
                        }
                        R.id.favorite_server -> {

                        }
                    }

                    false
                }
                show()
            }
        }
    }

    interface NetworkListener {
        fun changeServer(networkItem: NetworkItem)

    }
}

class NetworkDiff : DiffUtil.ItemCallback<NetWorkView>() {
    override fun areItemsTheSame(oldItem: NetWorkView, newItem: NetWorkView): Boolean {
        return oldItem.networkItem == newItem.networkItem
    }

    override fun areContentsTheSame(oldItem: NetWorkView, newItem: NetWorkView): Boolean {
        return oldItem.networkItem.nameNetwork == newItem.networkItem.nameNetwork
    }


}