package com.tapi.a0028speedtest.functions.maintab.adapters

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.a0028speedtest.R
import com.tapi.a0028speedtest.functions.maintab.objs.NetWorkViewItem
import com.tapi.a0028speedtest.functions.maintab.popup.ItemType
import com.tapi.a0028speedtest.functions.maintab.popup.NetworkPopupWindow

class NetworkProviderAdapter(val mContext: Context, val event: NetworkListener) : ListAdapter<NetWorkViewItem, NetworkProviderAdapter.NetworkProviderHolder>(NetworkDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkProviderHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_network, parent, false)
        return NetworkProviderHolder(view)
    }

    override fun onBindViewHolder(holder: NetworkProviderHolder, position: Int, payloads: MutableList<Any>) {
        val status = payloads.firstOrNull()
        if (status != null) {
            // update 1 phan
            holder.updateView(getItem(position).favorite)
        } else {
            // update full
            onBindViewHolder(holder, position)
        }
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
        holder.tvName.text = item.server.sponsor
        holder.tvLocation.text = item.server.country
        holder.tvDistance.text = item.distance.toString()
        if (item.favorite) {
            holder.ivFavorite.visibility = View.VISIBLE
        } else {
            holder.ivFavorite.visibility = View.GONE
        }
        if (item.isSelect) {
            holder.itemView.setBackgroundColor(Color.parseColor("#363950"))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.network_provider_item_bg)
        }
    }


    inner class NetworkProviderHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var tvName: TextView = itemView.findViewById(R.id.name_network_providers_item_tv)
        var tvLocation: TextView = itemView.findViewById(R.id.location_network_item_tv)
        var ivChoose: ImageView = itemView.findViewById(R.id.iv_choose)
        var tvDistance: TextView = itemView.findViewById(R.id.km)
        val ivFavorite: ImageView = itemView.findViewById(R.id.favorite)

        init {
            ivChoose.setOnClickListener(this)
            itemView.setOnClickListener {
                event.changeServer(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener(this)

        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.iv_choose -> {
                    showMenuChoose(v)
                }
            }
        }

        override fun onLongClick(v: View): Boolean {
            when(v.id){
                R.id.iv_choose ->{
                    showMenuChoose(v)
                }
            }
            return true
        }

        fun updateView(favorite: Boolean) {
            if (favorite) {
                ivFavorite.visibility = View.VISIBLE
            } else {
                ivFavorite.visibility = View.GONE
            }
        }

        private fun showMenuChoose(view: View) {
            val sortAudioPopupWindow = NetworkPopupWindow(view) {
                when (it) {
                    ItemType.FAVORITES -> {
                        event.setFavorite(getItem(adapterPosition))
                    }

                    ItemType.CHANGE -> {
                        event.changeServer(getItem(adapterPosition))
                    }
                }
            }
            sortAudioPopupWindow.show()
        }
    }

    interface NetworkListener {
        fun changeServer(netWorkViewItem: NetWorkViewItem)
        fun setFavorite(netWorkViewItem: NetWorkViewItem)
    }
}

class NetworkDiff : DiffUtil.ItemCallback<NetWorkViewItem>() {
    override fun areItemsTheSame(oldItem: NetWorkViewItem, newItem: NetWorkViewItem): Boolean {
        return oldItem.server.url == newItem.server.url
    }

    override fun areContentsTheSame(oldItem: NetWorkViewItem, newItem: NetWorkViewItem): Boolean {
//        return oldItem.networkItem.nameNetwork == newItem.networkItem.nameNetwork
        return false
    }

    override fun getChangePayload(oldItem: NetWorkViewItem, newItem: NetWorkViewItem): Any? {

        if (oldItem.favorite == oldItem.favorite) return newItem        // muon update cai nao thi check cai do khac, con lai bang nhau
        return null
    }
}