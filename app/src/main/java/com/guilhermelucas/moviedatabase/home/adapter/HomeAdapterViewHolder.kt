package com.guilhermelucas.moviedatabase.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.HomeRepository
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.DateFormat

class HomeAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(adapterItem: HomeRepository.AdapterItem, clickListener: HomeAdapter.MovieClickListener) {
        when (adapterItem) {
            is HomeRepository.AdapterMovieItem -> {
                val movie = adapterItem.movie
                itemView.textMovieTitle.text = movie.title

                val dateFormat =
                    DateFormat.getDateInstance(
                        DateFormat.YEAR_FIELD,
                        itemView.context.resources.configuration.locale
                    )
                itemView.textMovieReleaseYear.text = dateFormat.format(movie.releaseDate)
                itemView.setOnClickListener {
                    clickListener.onMovieClick(movie)
                }

                if (movie.voteAverage != null) {
                    itemView.textVoteAverage.visibility = View.VISIBLE
                    itemView.textVoteAverage.text = String.format("%.1f", movie.voteAverage)
                } else {
                    itemView.textVoteAverage.visibility = View.GONE
                }

                Glide.with(itemView)
                    .load(movie.posterUrl)
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(itemView.imageMoviePoster)
            }
            else -> {
                itemView.textMovieTitle.text = adapterItem.title
                Glide.with(itemView)
                    .load(itemView.context.getDrawable(R.drawable.ic_image_placeholder))
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(itemView.imageMoviePoster)
            }
        }


    }
}