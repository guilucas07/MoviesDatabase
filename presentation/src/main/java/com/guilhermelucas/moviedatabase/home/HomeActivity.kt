package com.guilhermelucas.moviedatabase.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.data.api.MovieDataSourceSettings
import com.guilhermelucas.data.api.MovieRemoteRepository
import com.guilhermelucas.data.firebase.RemoteConfig
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.base.BaseActivity
import com.guilhermelucas.moviedatabase.detail.DetailActivity
import com.guilhermelucas.moviedatabase.home.adapter.HomeAdapter
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.DeviceOrientation
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import kotlinx.android.synthetic.main.home_activity.*
import android.util.Pair as UtilPair

class HomeActivity : BaseActivity(), HomeContract.View {

    private lateinit var presenter: HomePresenter
    private var loadingMoreItems = false
    private val adapter by lazy {
        HomeAdapter(presenter as HomeAdapter.Presenter) {
            presenter.onItemClick(it)
        }
    }
    private var searchViewMenu: MenuItem? = null

    /******************************************/
    /**       Override activity methods      **/
    /******************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        setSupportActionBar(toolbar)

        val repository =
            HomeRepository(
                MovieImageUrlBuilder(RemoteConfig.instance),
                MovieRemoteRepository(
                    MovieDataSource.getInstance(MovieDataSourceSettings())
                )
            )
        presenter = HomePresenter(repository)
        presenter.attachView(this)

        initSwipeRefresh()
        initRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        initSearchMenu(menu)
        return true
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

    override fun goToDetail(movie: MovieVO) {
        val intent = Intent(baseContext, DetailActivity::class.java).apply {
            putExtra(DetailActivity.ExtraParam.ITEM_ID, movie.id)
        }

        startActivity(intent)
    }

    override fun loading(visible: Boolean) {
        loadingMoreItems = visible
        swipeRefreshMovies.isRefreshing = visible
    }

    override fun moviesLoaded() {
        adapter.notifyDataSetChanged()
    }

    override fun showError(error: HomeContract.Error) {
        val message = when (error) {
            HomeContract.Error.NETWORK -> R.string.request_error_network
            else -> {
                R.string.request_error_unknown
            }
        }
        showToast(message)
    }

    override fun getDeviceOrientation(): DeviceOrientation {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> DeviceOrientation.PORTRAIT
            else -> DeviceOrientation.LANDSCAPE
        }
    }

    override fun changeAdapterVisibility(visibility: HomeContract.AdapterVisibility) {
        when (visibility) {
            HomeContract.AdapterVisibility.DATA_VIEW -> {
                layoutEmptyMovies.visibility = View.GONE
                layoutEmptyMoviesSearch.visibility = View.GONE
                recyclerViewMovies.visibility = View.VISIBLE
            }
            HomeContract.AdapterVisibility.SEARCH_EMPTY_VIEW -> {
                layoutEmptyMovies.visibility = View.GONE
                layoutEmptyMoviesSearch.visibility = View.VISIBLE
                recyclerViewMovies.visibility = View.GONE
            }
            else -> {
                layoutEmptyMovies.visibility = View.VISIBLE
                layoutEmptyMoviesSearch.visibility = View.GONE
                recyclerViewMovies.visibility = View.GONE
            }
        }
    }

    /***********************/
    /**  private methods  **/
    /***********************/

    private fun initSearchMenu(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewMenu = menu?.findItem(R.id.search)
        val searchView = (searchViewMenu?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    presenter.searchMovie(it)
                }
                return false
            }
        })

        searchViewMenu?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return presenter.onCloseSearchBar()
            }

        })
    }

    private fun initSwipeRefresh() {
        swipeRefreshMovies.setOnRefreshListener {
            presenter.onSwipeToRefresh()
            searchViewMenu?.collapseActionView()
        }
    }

    private fun initRecycleView() {
        recyclerViewMovies.adapter = adapter
        recyclerViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        && totalItemCount >= PAGE_SIZE
                    ) {
                        loadingMoreItems = true
                        presenter.loadMoreItems()
                    }
                }
            }
        })

        val gridLayoutManager =
            GridLayoutManager(baseContext, presenter.getTotalItemsOnEachLine(), RecyclerView.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return presenter.getSpanSize(position)
            }
        }
        recyclerViewMovies.layoutManager = gridLayoutManager
    }
}


