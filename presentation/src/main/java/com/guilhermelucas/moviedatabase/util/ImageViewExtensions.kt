package com.guilhermelucas.moviedatabase.util

import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.guilhermelucas.moviedatabase.R

fun ImageView.loadFromUrl(url: String?) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .apply(requestOptions)
        .into(this)

fun ImageView.loadDrawable(@DrawableRes resourceId: Int) {
    setImageDrawable(ResourcesCompat.getDrawable(resources, resourceId, null))
}

private val factory by lazy {
    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
}
private val requestOptions by lazy {
    RequestOptions().placeholder(R.drawable.ic_image_placeholder).dontTransform()
}