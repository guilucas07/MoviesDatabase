package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.model.Movie
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(private val repository: HomeRepository,
                    private val imageUrlBuilder: MovieImageUrlBuilder) : HomeContract.Presenter {

    private var view: HomeContract.View? = null
    private var compositeDisposable = CompositeDisposable()
    private var activityMode = ActivityMode.DEFAULT
    private var isLoading = false

    /*****************************/
    /**     Private objects     **/
    /*****************************/
    private object Constants {
        const val SEARCH_MIN_LETTERS = 3
    }

    private enum class ActivityMode {
        DEFAULT, SEARCH
    }

    /******************************************/
    /**    HomeContract.Presenter methods   **/
    /******************************************/

    override fun attachView(view: HomeContract.View) {
        this.view = view
    }

    override fun onResume() {
        loadMoreItems()
    }

    override fun detachView() {
        this.view = null
        compositeDisposable.clear()
    }

    override fun onItemClick(movie: MovieVO) {
        view?.goToDetail(movie)
    }

    override fun loadMoreItems() {
        if (activityMode == ActivityMode.DEFAULT) {
            loadItems(HomeRepository.RequestStrategy.NEXT_PAGE)
        }
    }

    override fun onSwipeToRefresh() {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
    }

    override fun onCloseSearchBar(): Boolean {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
        return true
    }

    override fun searchMovie(partialName: String) {
        if (partialName.length >= Constants.SEARCH_MIN_LETTERS) {
            val disposable = repository.getMovie(partialName).subscribe { listMovies ->
                view?.clearAdapterItems()
                val listMovieVO = listMovies.map { movie ->
                    convertToMovieVO(movie)
                }
                view?.onLoadMovies(listMovieVO)
                activityMode = ActivityMode.SEARCH
            }
            compositeDisposable.add(disposable)
        }
    }

    /****************************/
    /**     private methods    **/
    /****************************/

    private fun loadItems(repositoryRequestStrategy: HomeRepository.RequestStrategy) {
        if (!isLoading) {
            isLoading = true
            view?.loading(isLoading)
            val disposable = repository.getDiscoveryMovies(repositoryRequestStrategy).subscribe {
                val listMovieVO = it.map { movie -> convertToMovieVO(movie) }

                if (repositoryRequestStrategy == HomeRepository.RequestStrategy.FIRST_PAGE)
                    view?.clearAdapterItems()

                view?.onLoadMovies(listMovieVO)

                activityMode = ActivityMode.DEFAULT
                isLoading = false
                view?.loading(isLoading)
            }
            compositeDisposable.add(disposable)
        }
    }

    private fun convertToMovieVO(movie: Movie): MovieVO {
        val posterUrl =
                if (movie.posterPath != null) imageUrlBuilder.buildPosterUrl(movie.posterPath) else null
        val backdropUrl =
                if (movie.backdropPath != null) imageUrlBuilder.buildBackdropUrl(movie.backdropPath) else null
        return MovieVO(movie.id, movie.title, movie.overview, movie.genres, movie.voteAverage, posterUrl, backdropUrl, movie.releaseDate)
    }
}