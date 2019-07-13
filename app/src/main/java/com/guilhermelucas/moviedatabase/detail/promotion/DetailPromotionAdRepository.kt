package com.guilhermelucas.moviedatabase.detail.promotion

import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterMovieItem
import com.guilhermelucas.moviedatabase.domain.model.Movie
import com.guilhermelucas.moviedatabase.domain.model.MovieVO
import com.guilhermelucas.moviedatabase.domain.model.toMovieVO
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
                val item = AdapterMovieItem(it.toMovieVO(imageUrlBuilder))
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

    //TODO add this on repository interface
    fun getLoadedItems() : Int = showedItems.size
}