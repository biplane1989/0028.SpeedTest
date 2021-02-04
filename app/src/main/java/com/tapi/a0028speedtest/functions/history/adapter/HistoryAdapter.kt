package com.tapi.a0028speedtest.functions.history.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tapi.a0028speedtest.database.objects.HistoryItem

class HistoryAdapter(val listener: (HistoryItem) -> Unit): ListAdapter<HistoryItem, HistoryItemViewHolder>(HistoryItemDiffUnit()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class HistoryItemDiffUnit: DiffUtil.ItemCallback<HistoryItem>() {
    override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem) = oldItem == newItem
}