package com.guilhermelucas.moviedatabase.detail.promotion

import android.content.Context
import android.os.Bundle
import com.guilhermelucas.moviedatabase.base.BaseTracker

class DetailPromotionAdTracker(context: Context) : BaseTracker(context) {

    private object Events {
        const val SCREEN_OPEN = "detail_promotion_screen_open"
    }

    fun logScreenOpen(searchKey: String) {
        val params = Bundle().apply {
            putString(CommonParams.SEARCH_KEY_API, searchKey)
        }
        logEvent(Events.SCREEN_OPEN, params)
    }

}