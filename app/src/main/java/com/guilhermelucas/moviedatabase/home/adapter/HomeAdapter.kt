package com.guilhermelucas.moviedatabase.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.HomeRepository
import com.guilhermelucas.moviedatabase.model.MovieVO

class HomeAdapter(
    private val movies: ArrayList<HomeRepository.AdapterItem>,
    private val clickListener: MovieClickListener
) :
    RecyclerView.Adapter<HomeAdapterViewHolder>() {

    interface MovieClickListener {
        fun onMovieClick(movie: MovieVO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return HomeAdapterViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) = holder.bind(movies[position], clickListener)

    fun addMoreItems(newMovies: List<HomeRepository.AdapterItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun clearItems() {
        movies.clear()
        notifyDataSetChanged()
    }
}
