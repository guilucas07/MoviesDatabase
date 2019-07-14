package com.guilhermelucas.moviedatabase.home.adapter.item

import com.guilhermelucas.moviedatabase.model.PromotionAd
import com.guilhermelucas.moviedatabase.model.MovieVO

sealed class AdapterItem{
    data class MovieItem(val movie: MovieVO) : AdapterItem()
    data class AdItem(val promotionAdItem: PromotionAd) : AdapterItem()

}