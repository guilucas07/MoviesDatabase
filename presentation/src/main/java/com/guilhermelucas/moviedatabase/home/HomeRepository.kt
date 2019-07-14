package com.guilhermelucas.moviedatabase.home

import android.content.Context
import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.data.firebase.RemoteConfig
import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.model.PromotionAd
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable
import java.util.*

class HomeRepository(
    private val context: Context,
    private val imageUrlBuilder: MovieImageUrlBuilder,
    private val movieDataSource: MovieDataSource,
    remoteConfig: RemoteConfig
) {

    private var actualPage: Int = 0
    private var promotionItemInterval = remoteConfig.getPromotionItemInterval().toInt() + 1

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    fun getMovie(partialName: String): Observable<List<AdapterItem>> {
        loadedItems.clear()
        return movieDataSource.getMovie(partialName)
            .map {
                it.forEach { movie ->
                    inflateAdapterItem(movie, false)
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

        return movieDataSource.getDiscoveryMovies(request, true).map { ret ->
            actualPage++
            ret.forEach {
                inflateAdapterItem(it)
            }
            loadedItems
        }
    }

    private fun inflateAdapterItem(movie: Movie, shouldAddAdItem: Boolean = true) {
        loadedItems.add(AdapterItem.MovieItem(movie.toMovieVO(imageUrlBuilder)))
        if (shouldAddAdItem && ((loadedItems.size + 1) % promotionItemInterval == 0))
            loadedItems.add(getNextAdItem())
    }

    private fun getNextAdItem(): AdapterItem.AdItem {
        return if (savePromotionAds.size > 1) {
            savePromotionAds[loadedItems.size % 2]
        } else
            savePromotionAds[0]
    }

    //must be from local repository
    private val savePromotionAds = arrayListOf(
        AdapterItem.AdItem(
            PromotionAd(
                String.format(context.getString(R.string.promotion_ad_title), "Marvel"),
                context.getString(R.string.promotion_ad_check_out),
                "marvel"
            )
        )
    )

    fun getAdapterItem(position: Int): AdapterItem? {
        if (position < loadedItems.size)
            return loadedItems[position]
        return null
    }

    fun getLoadedItems(): Int = loadedItems.size

    private val loadedItems = ArrayList<AdapterItem>()

}