package com.guilhermelucas.moviedatabase.home.adapter.item

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import kotlinx.android.synthetic.main.adapter_ad_item.view.*

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(adapterItem: AdapterAdItem, clickListener: (position: Int) -> Unit) {

        val promotion = adapterItem.promotionAdItem
        itemView.textAdTitle.text = promotion.title
        itemView.textAdAction.text = promotion.callToActionText
        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        Glide.with(itemView)
            .load(itemView.context.getDrawable(R.drawable.marvel_placeholder))
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(itemView.imageAdPoster)

    }
}