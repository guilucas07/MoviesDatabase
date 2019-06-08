package com.guilhermelucas.moviedatabase.base

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseTracker(val context: Context) {

    private val firabaseProviderTracker = FirebaseAnalytics.getInstance(context.applicationContext)

    object CommonParams{
        const val MOVIE_ID = "movie_id"
        const val SEARCH_KEY_API = "search_key_api"
    }

    fun logEvent(event: String, params: Bundle? = null) {
        firabaseProviderTracker.logEvent(event, params)
    }
}