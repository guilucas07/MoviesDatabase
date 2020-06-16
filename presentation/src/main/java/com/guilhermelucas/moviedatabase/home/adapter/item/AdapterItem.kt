package com.guilhermelucas.moviedatabase.home.adapter.item

import com.guilhermelucas.moviedatabase.model.MovieVO

sealed class AdapterItem{
    data class MovieItem(val movie: MovieVO) : AdapterItem()
}