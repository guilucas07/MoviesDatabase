package com.guilhermelucas.moviedatabase.home.adapter.item

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.util.loadFromUrl
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.DateFormat

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(adapterItem: AdapterMovieItem, clickListener: (position: Int) -> Unit) {

        val movie = adapterItem.movie
        itemView.textMovieTitle.text = movie.title

        val dateFormat =
            DateFormat.getDateInstance(
                DateFormat.YEAR_FIELD,
                itemView.context.resources.configuration.locale
            )
        itemView.textMovieReleaseYear.text = dateFormat.format(movie.releaseDate)
        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        if (movie.voteAverage != null) {
            itemView.textVoteAverage.visibility = View.VISIBLE
            itemView.textVoteAverage.text = String.format("%.1f", movie.voteAverage)
        } else {
            itemView.textVoteAverage.visibility = View.GONE
        }

        itemView.imageMoviePoster.loadFromUrl(movie.posterUrl)
    }
}