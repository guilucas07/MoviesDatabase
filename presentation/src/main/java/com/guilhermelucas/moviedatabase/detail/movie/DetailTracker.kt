package com.guilhermelucas.moviedatabase.detail.movie

import android.content.Context
import android.os.Bundle
import com.guilhermelucas.moviedatabase.base.BaseTracker

class DetailTracker(context: Context) : BaseTracker(context) {

    private object Events {
        const val SCREEN_OPEN = "detail_screen_open"
        const val SEE_BACKDROP = "detail_see_backdrop"
    }

    fun logScreenOpen(movieId: Int) {
        val params = Bundle().apply {
            putInt(CommonParams.MOVIE_ID, movieId)
        }
        logEvent(Events.SCREEN_OPEN, params)
    }

    fun logClickSeeBackdrop(movieId: Int){
        val params = Bundle().apply {
            putInt(CommonParams.MOVIE_ID, movieId)
        }
        logEvent(Events.SEE_BACKDROP, params)

    }
}