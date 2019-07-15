package com.guilhermelucas.moviedatabase.home.adapter.item

import android.view.View
import com.guilhermelucas.moviedatabase.util.loadFromUrl
import kotlinx.android.synthetic.main.adapter_movie_item.view.*
import java.text.DateFormat
import java.util.*

class MovieViewHolder(val clickListener: (position: Int) -> Unit, itemView: View) : AdapterViewHolder(itemView) {
    override fun bind(adapterItem: AdapterItem) {
        if (adapterItem !is AdapterItem.MovieItem)
            throw Exception("bindData adapter item should be AdapterItem.MovieItem")

        val movie = adapterItem.movie
        itemView.textMovieTitle.text = movie.title

        movie.releaseDate?.run {
            itemView.textMovieReleaseDate.text = this
        }

        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        itemView.imageMoviePoster.loadFromUrl(movie.posterUrl)
    }
}