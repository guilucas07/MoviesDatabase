package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.model.PromotionAd
import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.DeviceOrientation

interface HomeContract {
    interface View : BaseView {
        fun onLoadMovies(movies: List<AdapterItem>)
        fun goToDetail(movie: MovieVO)
        fun goToPromotionDetail(promotionAd: PromotionAd)
        fun loading(visible: Boolean)
        fun moviesLoaded()
        fun showError(error: Error)
        fun getDeviceOrientation(): DeviceOrientation
        fun changeAdapterVisibility(visibility: AdapterVisibility)
    }

    enum class AdapterVisibility {
        EMPTY_VIEW, SEARCH_EMPTY_VIEW, DATA_VIEW
    }

    interface Presenter : BasePresenter<View>, HomeAdapter.Presenter {
        fun onResume()
        fun onItemClick(position: Int)
        fun loadMoreItems()
        fun onSwipeToRefresh()
        fun searchMovie(partialName: String)
        fun onCloseSearchBar(): Boolean
    }

    enum class Error {
        NETWORK, REQUEST_GENERIC_ERROR
    }
}