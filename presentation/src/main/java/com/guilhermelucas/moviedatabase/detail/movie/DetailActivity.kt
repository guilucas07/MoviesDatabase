package com.guilhermelucas.moviedatabase.detail.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.data.firebase.RemoteConfig
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.base.BaseActivity
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import com.guilhermelucas.moviedatabase.util.loadFromUrl
import com.guilhermelucas.moviedatabase.viewer.ImageViewer
import kotlinx.android.synthetic.main.detail_activity.*
import java.text.DateFormat
import java.util.*

class DetailActivity : BaseActivity(), DetailContract.View {

    private lateinit var presenter: DetailPresenter

    object ExtraParam {
        const val ITEM_ID = "detail_activity_param_item_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.title = ""
        }

        val itemId = intent?.extras?.getInt(ExtraParam.ITEM_ID, -1) ?: -1
        if (itemId == -1)
            throw Throwable(
                "${ExtraParam.ITEM_ID} is necessary to this activity and " +
                        "doesn't was informed. Check startActivity and try again'"
            )

        presenter = DetailPresenter(
            itemId,
            DetailRepository(
                MovieDataSource.instance,
                MovieImageUrlBuilder(RemoteConfig.instance)
            ),
            DetailTracker(baseContext)
        )
        presenter.attachView(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun loadMovieDetail(movie: MovieVO) {
        textMovieTitle.text = movie.title
        textMovieOverview.text = movie.overview
        textMovieGenres.text = movie.genres?.joinToString(separator = ", ") { it.name }

        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        textMovieReleaseDate.text = dateFormat.format(movie.releaseDate)

        imageMoviePoster.loadFromUrl(movie.posterUrl)
        imageMovieBackdrop.loadFromUrl(movie.backdropUrl)

        imageMovieBackdrop.setOnClickListener {
            presenter.seeMovieBackdrop(movie)
        }

        if (textMovieOverview.text.isNullOrEmpty()) {
            textOverview.visibility = View.GONE
            textMovieOverview.visibility = View.GONE
        }

    }

    override fun goToBackdropViewer(url: String) {
        val intent = Intent(this, ImageViewer::class.java).apply {
            putExtra(ImageViewer.ExtraParam.ITEM_URL, url)
        }

        startActivity(intent)
    }

    override fun showError(error: DetailContract.Error) {
        val message = when (error) {
            DetailContract.Error.EMPTY_BACKDROP ->
                R.string.detail_error_backdrop_unavailable
            DetailContract.Error.EMPTY_POSTER ->
                R.string.detail_error_poster_unavailable
            DetailContract.Error.NETWORK ->
                R.string.request_error_network
            DetailContract.Error.REQUEST_GENERIC_ERROR ->
                R.string.request_error_unknown
        }

        showToast(message)
    }

    override fun close() {
        super.onBackPressed()
    }
}