package com.guilhermelucas.moviedatabase.home.adapter.item

import android.view.View
import com.guilhermelucas.moviedatabase.util.loadFromUrl
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.DateFormat
import java.util.*

class MovieViewHolder(val clickListener: (position: Int) -> Unit, itemView: View) : AdapterViewHolder(itemView) {
    override fun bind(adapterItem: AdapterItem) {
        if (adapterItem !is AdapterItem.MovieItem)
            throw Exception("bindData adapter item should be AdapterItem.MovieItem")

        val movie = adapterItem.movie
        itemView.textMovieTitle.text = movie.title

        val dateFormat =
            DateFormat.getDateInstance(
                DateFormat.YEAR_FIELD,
                Locale.getDefault()
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