package com.guilhermelucas.moviedatabase.base

import android.content.Context
import com.guilhermelucas.data.Analytics

abstract class BaseTracker(context: Context) : Analytics(context) {
    object CommonParams{
        const val MOVIE_ID = "movie_id"
        const val SEARCH_KEY_API = "search_key_api"
    }
}