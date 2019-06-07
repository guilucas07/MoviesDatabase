package com.guilhermelucas.moviedatabase.home.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.HomeRepository
import com.guilhermelucas.moviedatabase.model.MovieVO
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.DateFormat

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
        Log.d("ImprimirFilmes", "ImprimirF AddMoreItem ${movies.size}" )
    }

    fun clearItems() {
        movies.clear()
        notifyDataSetChanged()
    }
}
