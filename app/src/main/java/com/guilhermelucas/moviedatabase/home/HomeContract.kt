package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.model.MovieVO

interface HomeContract {
    interface View : BaseView {
        fun onLoadMovies(movies: List<MovieVO>)
        fun goToDetail(movie: MovieVO)
        fun loading(visible: Boolean)
        fun clearAdapterItems()
    }

    interface Presenter : BasePresenter<View> {
        fun onResume()
        fun onItemClick(movie: MovieVO)
        fun loadMoreItems()
        fun onSwipeToRefresh()
        fun searchMovie(partialName: String)
        fun onCloseSearchBar(): Boolean
    }
}