package com.guilhermelucas.moviedatabase.detail.movie

import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.model.MovieVO

interface DetailContract {
    interface View : BaseView {
        fun loadMovieDetail(movie: MovieVO)
        fun goToBackdropViewer(url: String)
        fun showError(error: Errors)
    }

    interface Presenter : BasePresenter<View> {
        fun onResume()
        fun seeMovieBackdrop(movie: MovieVO)
    }

    enum class Errors {
        EMPTY_BACKDROP, EMPTY_POSTER
    }
}