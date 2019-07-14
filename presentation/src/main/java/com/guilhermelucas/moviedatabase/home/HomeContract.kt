package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.model.PromotionAd
import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.MovieVO

interface HomeContract {
    interface View : BaseView {
        fun onLoadMovies(movies: List<AdapterItem>)
        fun goToDetail(movie: MovieVO)
        fun goToPromotionDetail(promotionAd: PromotionAd)
        fun loading(visible: Boolean)
        fun moviesLoaded()
        fun showError(error: Failure)
    }

    interface Presenter : BasePresenter<View>, HomeAdapter.Presenter {
        fun onResume()
        fun onItemClick(position: Int)
        fun loadMoreItems()
        fun onSwipeToRefresh()
        fun searchMovie(partialName: String)
        fun onCloseSearchBar(): Boolean
    }

    sealed class Failure(val errorMessage: String) {
        class NetworkConnection : Failure("Verify your internet connection and try again!")
        class GenericFailure(errorMessage: String) : Failure(errorMessage)

    }
}