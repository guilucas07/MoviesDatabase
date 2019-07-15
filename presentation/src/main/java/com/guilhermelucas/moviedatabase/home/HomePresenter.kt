package com.guilhermelucas.moviedatabase.home

import com.guilhermelucas.data.BaseSchedulerProvider
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterViewHolder
import com.guilhermelucas.moviedatabase.util.DeviceOrientation
import com.guilhermelucas.moviedatabase.util.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

class HomePresenter(
    private val repository: HomeRepository,
    private val schedulers: BaseSchedulerProvider = SchedulerProvider()
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
        const val MAX_ITEMS_EACH_ROW_PORTRAIT = 2
        const val MAX_ITEMS_EACH_ROW_LANDSCAPE = 3
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
        if (adapterItem != null) {
            if (adapterItem is AdapterItem.MovieItem)
                view?.goToDetail(adapterItem.movie)
            else if (adapterItem is AdapterItem.AdItem)
                view?.goToPromotionDetail(adapterItem.promotionAdItem)
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
            return if (adapterItem is AdapterItem.AdItem) getTotalItemsOnEachLine() else 1
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
                .observeOn(schedulers.ui())
                .subscribeOn(schedulers.io())
                .subscribe({ listMovies ->
                    view?.onLoadMovies(listMovies)
                    if (listMovies.isNotEmpty()) {
                        view?.changeAdapterVisibility(HomeContract.AdapterVisibility.DATA_VIEW)
                    } else
                        view?.changeAdapterVisibility(HomeContract.AdapterVisibility.SEARCH_EMPTY_VIEW)

                    activityMode = ActivityMode.SEARCH
                }, {
                    activityMode = ActivityMode.DEFAULT
                    view?.showError(getErrorType(it))
                })

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
                    .observeOn(schedulers.ui())
                    .subscribeOn(schedulers.io())
                    .subscribe({ listMovies ->
                        view?.onLoadMovies(listMovies)

                        if (listMovies.isNotEmpty()) {
                            view?.changeAdapterVisibility(HomeContract.AdapterVisibility.DATA_VIEW)
                        } else
                            view?.changeAdapterVisibility(HomeContract.AdapterVisibility.EMPTY_VIEW)

                        activityMode = ActivityMode.DEFAULT
                        isLoading = false
                        view?.loading(isLoading)
                    }, {
                        activityMode = ActivityMode.DEFAULT
                        isLoading = false
                        view?.loading(isLoading)
                        view?.showError(getErrorType(it))
                    })
            compositeDisposable.add(observer)

        }
    }

    private fun getErrorType(throwable: Throwable): HomeContract.Error {
        return when (throwable) {
            is UnknownHostException -> HomeContract.Error.NETWORK
            else -> HomeContract.Error.REQUEST_GENERIC_ERROR
        }
    }

    override fun onBindRepositoryRowViewAtPosition(holder: AdapterViewHolder, position: Int) {
        repository.getAdapterItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemsCount(): Int {
        return repository.getLoadedItems()
    }

    override fun getItemViewHolder(adapterPosition: Int): HomeAdapter.ViewHolderType {
        val item = repository.getAdapterItem(adapterPosition)

        return when (item) {
            is AdapterItem.AdItem -> HomeAdapter.ViewHolderType.AD
            else -> HomeAdapter.ViewHolderType.MOVIE
        }
    }

    override fun getTotalItemsOnEachLine(): Int {
        return if (view?.getDeviceOrientation() == DeviceOrientation.PORTRAIT)
            Constants.MAX_ITEMS_EACH_ROW_PORTRAIT
        else
            Constants.MAX_ITEMS_EACH_ROW_LANDSCAPE
    }
}