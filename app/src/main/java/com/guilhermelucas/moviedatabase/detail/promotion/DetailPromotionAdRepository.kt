package com.guilhermelucas.moviedatabase.detail.promotion

import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterMovieItem
import com.guilhermelucas.moviedatabase.domain.model.Movie
import com.guilhermelucas.moviedatabase.domain.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable
import java.text.FieldPosition

class DetailPromotionAdRepository(
    private val movieDataSource: MovieDataSource,
    private val imageUrlBuilder: MovieImageUrlBuilder
) {

    private val showedItems = ArrayList<AdapterItem>()

    fun getMovies(partialName: String): Observable<List<AdapterItem>> {
        return movieDataSource.getMovie(partialName).flatMap { ret ->
            Observable.fromIterable(ret).map {
                val item = AdapterMovieItem(convertToMovieVO(it))
                showedItems.add(item)
                item
            }.toList().toObservable()
        }
    }

    fun getAdapterItem(position: Int): AdapterItem? {
        if (position < showedItems.size)
            return showedItems[position]
        return null
    }

    private fun convertToMovieVO(movie: Movie): MovieVO {
        var posterUrl: String? = null
        var backdropUrl: String? = null

        movie.posterPath?.let {
            posterUrl = imageUrlBuilder.buildPosterUrl(it)
        }

        movie.backdropPath?.let {
            backdropUrl = imageUrlBuilder.buildBackdropUrl(it)
        }

        val genres = if (!movie.genres.isNullOrEmpty()) {
            movie.genres
        } else {
            Cache.genres.filter { movie.genreIds?.contains(it.id) == true }
        }

        return MovieVO(
            movie.id,
            movie.title,
            movie.overview,
            genres,
            movie.voteAverage,
            posterUrl,
            backdropUrl,
            movie.releaseDate
        )
    }

}