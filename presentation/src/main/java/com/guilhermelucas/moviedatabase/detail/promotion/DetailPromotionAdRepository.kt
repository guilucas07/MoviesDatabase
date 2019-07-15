package com.guilhermelucas.moviedatabase.detail.promotion

import com.guilhermelucas.data.api.MovieRemoteRepository
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable

class DetailPromotionAdRepository(
    private val movieRepository: MovieRemoteRepository,
    private val imageUrlBuilder: MovieImageUrlBuilder
) {

    private val showedItems = ArrayList<AdapterItem>()

    fun getMovies(partialName: String): Observable<List<AdapterItem>> {
        return movieRepository.getMovie(partialName, true).flatMap { ret ->
            Observable.fromIterable(ret).map {
                val item = AdapterItem.MovieItem(it.toMovieVO(imageUrlBuilder))
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
    fun getLoadedItems(): Int = showedItems.size
}