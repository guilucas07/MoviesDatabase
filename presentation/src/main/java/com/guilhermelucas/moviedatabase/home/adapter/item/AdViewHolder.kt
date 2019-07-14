package com.guilhermelucas.moviedatabase.home.adapter.item

import android.view.View
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.util.loadDrawable
import kotlinx.android.synthetic.main.adapter_ad_item.view.*

class AdViewHolder(val clickListener: (position: Int) -> Unit, itemView: View) : AdapterViewHolder(itemView) {

    override fun bind(adapterItem: AdapterItem) {
        if (adapterItem !is AdapterItem.AdItem)
            throw Exception("bindData adapter item should be AdapterItem.AdItem")

        val promotion = adapterItem.promotionAdItem
        itemView.textAdTitle.text = promotion.title
        itemView.textAdAction.text = promotion.callToActionText
        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        itemView.imageAdPoster.loadDrawable(R.drawable.marvel_placeholder)
    }
}