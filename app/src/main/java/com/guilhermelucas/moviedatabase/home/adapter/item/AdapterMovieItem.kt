package com.guilhermelucas.moviedatabase.home.adapter.item

import com.guilhermelucas.moviedatabase.model.MovieVO

class AdapterMovieItem(val movie: MovieVO) : AdapterItem() {
    override val title: String
        get() = movie.title

}