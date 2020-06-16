package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.data.api.MovieRemoteRepository
import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable
import java.util.*

class HomeRepository(
    private val imageUrlBuilder: MovieImageUrlBuilder,
    private val movieRepository: MovieRemoteRepository
) {

    private var actualPage: Int = 0

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    fun getMovie(partialName: String): Observable<List<AdapterItem>> {
        loadedItems.clear()
        return movieRepository.getMovie(partialName)
            .map {
                it.forEach { movie ->
                    inflateAdapterItem(movie)
                }
                loadedItems
            }
    }

    fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): Observable<List<AdapterItem>> {
        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }

        if (request == 1)
            loadedItems.clear()

        return movieRepository.getUpcomingMovies(request, true).map { ret ->
            actualPage++
            ret.forEach {
                inflateAdapterItem(it)
            }
            loadedItems
        }
    }

    private fun inflateAdapterItem(movie: Movie) {
        loadedItems.add(AdapterItem.MovieItem(movie.toMovieVO(imageUrlBuilder)))
    }

    fun getAdapterItem(position: Int): AdapterItem? {
        if (position < loadedItems.size)
            return loadedItems[position]
        return null
    }

    fun getLoadedItems(): Int = loadedItems.size

    private val loadedItems = ArrayList<AdapterItem>()

}