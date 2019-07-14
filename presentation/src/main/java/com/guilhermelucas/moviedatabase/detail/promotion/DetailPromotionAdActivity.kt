package com.guilhermelucas.moviedatabase.detail.promotion

import android.content.Intent
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.data.firebase.RemoteConfig
import com.guilhermelucas.domain.PromotionAd
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.base.BaseActivity
import com.guilhermelucas.moviedatabase.detail.movie.DetailActivity
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import kotlinx.android.synthetic.main.detail_promotion_activity.*

class DetailPromotionAdActivity : BaseActivity(), DetailPromotionAdContract.View {

    object ExtraParam {
        const val SERIALIZABLE_PROMOTION_AD = "detail_promotion_ad"
    }

    private lateinit var presenter: DetailPromotionAdContract.Presenter
    private var loadingMoreItems = false
    private val adapter by lazy {
        HomeAdapter(presenter as HomeAdapter.Presenter) {
            presenter.onItemClick(it)
        }
    }

    override fun goToDetail(movie: MovieVO) {
        val intent = Intent(baseContext, DetailActivity::class.java).apply {
            putExtra(DetailActivity.ExtraParam.ITEM_ID, movie.id)
        }

        startActivity(intent)
    }

    override fun setTitleText(title: String) {
        textPromotionAdTitle.text = title
    }

    /******************************************/
    /**       Override activity methods      **/
    /******************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_promotion_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.title = ""
        }

        val promotionAd =
            intent?.extras?.getSerializable(ExtraParam.SERIALIZABLE_PROMOTION_AD)
                ?: throw IllegalArgumentException(
                    "${ExtraParam.SERIALIZABLE_PROMOTION_AD} is necessary to this activity and " +
                            "doesn't was informed. Check startActivity and try again'"
                )
        val repository =
            DetailPromotionAdRepository(
                MovieDataSource.instance,
                MovieImageUrlBuilder(RemoteConfig.instance)
            )

        presenter = DetailPromotionAdPresenter(
            promotionAd as PromotionAd,
            repository,
            DetailPromotionAdTracker(baseContext)
        )
        presenter.attachView(this)

        initRecycleView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    /******************************************/
    /**  Override HomeContract.View methods  **/
    /******************************************/
    override fun onLoadMovies(movies: List<AdapterItem>) {
        adapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /***********************/
    /**  Private methods  **/
    /***********************/
    private fun initRecycleView() {
        recyclerPromotionAdMovies.adapter = adapter
        recyclerPromotionAdMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager? ?: return

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!loadingMoreItems) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= MifareUltralight.PAGE_SIZE
                    ) {
                        loadingMoreItems = true
                        presenter.loadMoreItems()
                    }
                }
            }
        })

        val gridLayoutManager = GridLayoutManager(baseContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerPromotionAdMovies.layoutManager = gridLayoutManager
    }

}