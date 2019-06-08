package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterAdItem
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterMovieItem
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
    private var shouldLoadPromotionAds = false


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

    override fun onItemClick(position: Int) {
        val adapterItem = repository.getAdapterItem(position)
        adapterItem?.run {
            if (this is AdapterMovieItem)
                view?.goToDetail(this.movie)
            else (this is AdapterAdItem)
            view?.goToPromotionDetail((this as AdapterAdItem).promotionAdItem)
        }
    }

    override fun loadMoreItems() {
        if (activityMode == ActivityMode.DEFAULT) {
            loadItems(HomeRepository.RequestStrategy.NEXT_PAGE)
        }
    }

    override fun getSpanSize(adapterPosition: Int): Int {
        if (shouldLoadPromotionAds) {
            val adapterItem = repository.getAdapterItem(adapterPosition)
            return if (adapterItem is AdapterAdItem) 2 else 1
        }
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
            shouldLoadPromotionAds = false
            val disposable = repository.getMovie(partialName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { listMovies ->
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

            shouldLoadPromotionAds = true
            this.repositoryRequestStrategy = repositoryRequestStrategy

            val observer =
                repository.loadMoreData(repositoryRequestStrategy)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
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