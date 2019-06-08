package com.guilhermelucas.moviedatabase.detail.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.api.MovieDataSource
import com.guilhermelucas.moviedatabase.base.BaseActivity
import com.guilhermelucas.moviedatabase.firebase.RemoteConfig
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import com.guilhermelucas.moviedatabase.viewer.ImageViewer
import kotlinx.android.synthetic.main.detail_activity.*
import java.text.DateFormat


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
            throw IllegalArgumentException(
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

        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, resources.configuration.locale)
        textMovieReleaseDate.text = dateFormat.format(movie.releaseDate)
        Glide.with(baseContext)
            .load(movie.posterUrl)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(imageMoviePoster)

        movie.backdropUrl?.let {
            textMovieBackdrop.visibility = View.VISIBLE
            textMovieBackdrop.setOnClickListener {
                presenter.seeMovieBackdrop(movie)
            }
        }

        if (textMovieOverview.text.isNullOrEmpty())
            textMovieOverview.visibility = View.GONE

    }

    override fun goToBackdropViewer(url: String) {
        val intent = Intent(this, ImageViewer::class.java).apply {
            putExtra(ImageViewer.ExtraParam.ITEM_URL, url)
        }

        startActivity(intent)
    }

    override fun showError(error: DetailContract.Errors) {
        when (error) {
            DetailContract.Errors.EMPTY_BACKDROP -> Toast.makeText(
                this,
                R.string.detail_error_backdrop_unavailable,
                Toast.LENGTH_SHORT
            ).show()
            DetailContract.Errors.EMPTY_POSTER -> Toast.makeText(
                this,
                R.string.detail_error_poster_unavailable,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}