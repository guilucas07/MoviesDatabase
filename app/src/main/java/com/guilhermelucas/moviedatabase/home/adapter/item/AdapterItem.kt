package com.guilhermelucas.moviedatabase.home.adapter.item

import com.guilhermelucas.moviedatabase.domain.model.MovieVO
import com.guilhermelucas.moviedatabase.domain.model.PromotionAd

sealed class AdapterItem{
    data class MovieItem(val movie: MovieVO) : AdapterItem()
    data class AdItem(val promotionAdItem: PromotionAd) : AdapterItem()

}