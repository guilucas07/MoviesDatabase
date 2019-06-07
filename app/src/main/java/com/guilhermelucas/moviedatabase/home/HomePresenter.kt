package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterAdItem
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(
    private val repository: HomeRepository
) : HomeContract.Presenter {

    private var view: HomeContract.View? = null
    private var compositeDisposable = CompositeDisposable()
    private var activityMode = ActivityMode.DEFAULT
    private var isLoading = false
    private var repositoryRequestStrategy = HomeRepository.RequestStrategy.FIRST_PAGE


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

    override fun getSpanSize(adapterPosition : Int) : Int {
        val adapterItem = repository.getAdapterItem(adapterPosition)
        if(adapterItem is AdapterAdItem)
            return 2
        else
            return 1
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
            val disposable = repository.getMovie(partialName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { listMovies ->
                    view?.clearAdapterItems()
                    view?.onLoadMovies(listMovies)
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

            this.repositoryRequestStrategy = repositoryRequestStrategy

            val observer =
                repository.loadMoreData(repositoryRequestStrategy)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        if (repositoryRequestStrategy == HomeRepository.RequestStrategy.FIRST_PAGE)
                            view?.clearAdapterItems()

                        view?.onLoadMovies(it)

                        activityMode = ActivityMode.DEFAULT
                        isLoading = false
                        view?.loading(isLoading)

                    }.doOnComplete {
                        activityMode = ActivityMode.DEFAULT
                        isLoading = false
                        view?.loading(isLoading)

                    }.doOnError {
                        activityMode = ActivityMode.DEFAULT
                        isLoading = false
                        view?.loading(isLoading)
                    }.subscribe()
            compositeDisposable.add(observer)

        }
    }
}