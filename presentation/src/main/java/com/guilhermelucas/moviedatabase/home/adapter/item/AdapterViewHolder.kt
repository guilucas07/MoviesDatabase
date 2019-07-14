package com.guilhermelucas.moviedatabase.home.adapter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(adapterItem: AdapterItem)
}