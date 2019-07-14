package com.guilhermelucas.moviedatabase.detail.promotion

import com.guilhermelucas.moviedatabase.base.BasePresenter
import com.guilhermelucas.moviedatabase.base.BaseView
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter

interface DetailPromotionAdContract {
    interface View : BaseView {
        fun onLoadMovies(movies: List<AdapterItem>)
        fun goToDetail(movie: MovieVO)
        fun setTitleText(title : String)
    }

    interface Presenter : BasePresenter<View>, HomeAdapter.Presenter {
        fun onResume()
        fun loadMoreItems()
        fun onItemClick(position: Int)
    }

}