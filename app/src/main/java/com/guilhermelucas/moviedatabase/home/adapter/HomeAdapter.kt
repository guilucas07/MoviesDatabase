package com.guilhermelucas.moviedatabase.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.adapter.item.*

class HomeAdapter(
    private var movies: List<AdapterItem>,
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ad_item, parent, false)
            AdViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
            MovieViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movies[position] is AdapterAdItem) 0 else 1
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder)
            return holder.bind(movies[position] as AdapterMovieItem, clickListener)
        else if (holder is AdViewHolder)
            return holder.bind(movies[position] as AdapterAdItem, clickListener)
    }

    fun addMoreItems(newMovies: List<AdapterItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
