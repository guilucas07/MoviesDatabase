package com.guilhermelucas.moviedatabase.home

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.model.MovieVO
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.DateFormat

class HomeAdapter(
    private val movies: ArrayList<HomeRepository.AdapterItem>,
    private val clickListener: MovieClickListener
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    interface MovieClickListener {
        fun onMovieClick(movie: MovieVO)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(adapterItem: HomeRepository.AdapterItem, clickListener: MovieClickListener) {
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
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position], clickListener)

    fun addMoreItems(newMovies: List<HomeRepository.AdapterItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
        Log.d("ImprimirFilmes", "ImprimirF AddMoreItem ${movies.size}" )
    }

    fun clearItems() {
        movies.clear()
        notifyDataSetChanged()
    }
}
