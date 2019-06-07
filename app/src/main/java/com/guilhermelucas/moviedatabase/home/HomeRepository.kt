package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.data.Cache
import com.guilhermelucas.moviedatabase.model.Genre
import com.guilhermelucas.moviedatabase.model.Movie
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*
import kotlin.collections.ArrayList

class HomeRepository(private val imageUrlBuilder: MovieImageUrlBuilder, private val movieDataSource: MovieDataSource) {

    private var actualPage: Int = 0

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    fun getUpcomingMovies(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): Observable<List<Movie>> {

        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }
        val genresObservable = movieDataSource.getGenres()
        val upcomingMoviesObservable = movieDataSource.getUpcomingMovies(request)

        return Observable.zip<List<Genre>, List<Movie>, List<Movie>>(
            genresObservable,
            upcomingMoviesObservable,
            BiFunction<List<Genre>, List<Movie>, List<Movie>> { genres, movies ->
                actualPage++
                val moviesWithGenres = movies.map { movie ->
                    movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                }
                Cache.cacheMovies(moviesWithGenres)
                moviesWithGenres
            })
    }

    fun getMovie(partialName: String): Observable<List<AdapterItem>> {
        return movieDataSource.getMovie(partialName).flatMap { ret ->
            Observable.fromIterable(ret).map {
                AdapterMovieItem(convertToMovieVO(it))
            }.toList().toObservable()
        }
    }

    private fun getDiscoveryMovies(request: Int): Observable<List<Movie>> {
        val genresObservable = movieDataSource.getGenres()
        val upcomingMoviesObservable = movieDataSource.getDiscoveryMovies(request)

        return Observable.zip<List<Genre>, List<Movie>, List<Movie>>(
            genresObservable,
            upcomingMoviesObservable,
            BiFunction<List<Genre>, List<Movie>, List<Movie>> { genres, movies ->
                actualPage++
                val moviesWithGenres = movies.map { movie ->
                    movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                }
                Cache.cacheMovies(moviesWithGenres)
                moviesWithGenres
            })
    }

    fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): Observable<List<AdapterItem>> {

        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }

        if (request == 1)
            loadedItems.clear()

        return getDiscoveryMovies(request).map { ret ->
            ret.forEach {
                inflateAdapterItem(it)
            }
            loadedItems
        }
    }

    private fun inflateAdapterItem(movie: Movie) {
        if ((loadedItems.size + 1) % 5 == 0)
            loadedItems.add(AdapterAdItem())

        loadedItems.add(AdapterMovieItem(convertToMovieVO(movie)))
    }

    fun getAdapterItem(position: Int): AdapterItem? {
        if (position < loadedItems.size)
            return loadedItems[position]
        return null
    }

    private val loadedItems = ArrayList<AdapterItem>()

    abstract class AdapterItem {
        abstract val title: String
    }

    class AdapterMovieItem(val movie: MovieVO) : AdapterItem() {
        override val title: String
            get() = movie.title

    }

    class AdapterAdItem : AdapterItem() {
        override val title: String
            get() = "Marvel Add"

        val time = Date().time
    }

    private fun convertToMovieVO(movie: Movie): MovieVO {
        val posterUrl =
            if (movie.posterPath != null) imageUrlBuilder.buildPosterUrl(movie.posterPath) else null
        val backdropUrl =
            if (movie.backdropPath != null) imageUrlBuilder.buildBackdropUrl(movie.backdropPath) else null
        return MovieVO(
            movie.id,
            movie.title,
            movie.overview,
            movie.genres,
            movie.voteAverage,
            posterUrl,
            backdropUrl,
            movie.releaseDate
        )
    }


}