package com.guilhermelucas.moviedatabase.home.adapter.item

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.util.loadDrawable
import com.guilhermelucas.moviedatabase.util.loadFromUrl
import kotlinx.android.synthetic.main.adapter_ad_item.view.*

class AdViewHolder(val clickListener: (position: Int) -> Unit, itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(adapterItem: AdapterAdItem) {
        val promotion = adapterItem.promotionAdItem
        itemView.textAdTitle.text = promotion.title
        itemView.textAdAction.text = promotion.callToActionText
        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        itemView.imageAdPoster.loadDrawable(R.drawable.marvel_placeholder)
    }
}