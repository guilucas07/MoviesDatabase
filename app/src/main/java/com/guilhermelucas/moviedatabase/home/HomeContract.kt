package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.domain.model.MovieVO
import com.guilhermelucas.moviedatabase.domain.model.PromotionAd
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter

interface HomeContract {
    interface View : BaseView {
        fun onLoadMovies(movies: List<AdapterItem>)
        fun goToDetail(movie: MovieVO)
        fun goToPromotionDetail(promotionAd : PromotionAd)
        fun loading(visible: Boolean)
        fun moviesLoaded()
    }

    interface Presenter : BasePresenter<View>, HomeAdapter.Presenter {
        fun onResume()
        fun onItemClick(position: Int)
        fun loadMoreItems()
        fun onSwipeToRefresh()
        fun searchMovie(partialName: String)
        fun onCloseSearchBar(): Boolean
    }
}