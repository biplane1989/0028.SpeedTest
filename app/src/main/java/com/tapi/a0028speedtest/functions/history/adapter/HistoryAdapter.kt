package com.tapi.a0028speedtest.functions.history.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tapi.a0028speedtest.data.History

class HistoryAdapter(val listener: (History) -> Unit): ListAdapter<History, HistoryItemViewHolder>(HistoryItemDiffUnit()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class HistoryItemDiffUnit: DiffUtil.ItemCallback<History>() {
    override fun areItemsTheSame(oldItem: History, newItem: History) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: History, newItem: History) = oldItem == newItem
}