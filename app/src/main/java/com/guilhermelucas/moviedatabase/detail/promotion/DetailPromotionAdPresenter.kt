package com.guilhermelucas.moviedatabase.detail.promotion

import android.support.v7.widget.RecyclerView
import com.guilhermelucas.moviedatabase.domain.model.PromotionAd
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdViewHolder
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterViewHolder
import com.guilhermelucas.moviedatabase.home.adapter.item.MovieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailPromotionAdPresenter(
    private val promotionAd: PromotionAd,
    private val repository: DetailPromotionAdRepository,
    private val tracker: DetailPromotionAdTracker
) : DetailPromotionAdContract.Presenter {

    private var view: DetailPromotionAdContract.View? = null
    private var isLoading = false
    private var compositeDisposable = CompositeDisposable()

    /*****************************************************/
    /**   DetailPromotionAdContract.Presenter methods   **/
    /*****************************************************/
    override fun attachView(view: DetailPromotionAdContract.View) {
        this.view = view
        tracker.logScreenOpen(promotionAd.searchKey)
    }

    override fun detachView() {
        view = null
    }

    override fun onResume() {
        loadItems()
        view?.setTitleText(promotionAd.title)
    }

    override fun loadMoreItems() {
        loadItems()
    }

    override fun onItemClick(position: Int) {
        val adapterItem = repository.getAdapterItem(position)
        adapterItem?.run {
            if (this is AdapterItem.MovieItem)
                view?.goToDetail(this.movie)
        }
    }

    override fun onBindRepositoryRowViewAtPosition(holder: AdapterViewHolder, position: Int) {
        return holder.bind(repository.getAdapterItem(position) as AdapterItem.MovieItem)
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

    override fun getSpanSize(adapterPosition: Int): Int {
        return 1
    }

    /*************************/
    /**   Private methods   **/
    /*************************/
    private fun loadItems() {
        if (!isLoading) {
            isLoading = true

            val observer = repository.getMovies(promotionAd.searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    view?.onLoadMovies(items)
                    isLoading = false
                }
            compositeDisposable.add(observer)

        }
    }


}