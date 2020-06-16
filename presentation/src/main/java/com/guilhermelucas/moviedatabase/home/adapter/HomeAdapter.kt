package com.guilhermelucas.moviedatabase.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterViewHolder
import com.guilhermelucas.moviedatabase.home.adapter.item.MovieViewHolder
import java.lang.IllegalArgumentException

class HomeAdapter(
    private val presenter: Presenter,
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<AdapterViewHolder>() {

    enum class ViewHolderType {
        MOVIE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return if (viewType == ViewHolderType.MOVIE.ordinal) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_movie_item, parent, false)
            MovieViewHolder(clickListener, view)
        }else
            throw IllegalArgumentException("$viewType int does not valid for this adapter. See ViewHolderType class")
    }

    override fun getItemViewType(position: Int): Int = presenter.getItemViewHolder(position).ordinal

    override fun getItemCount() = presenter.getItemsCount()

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) =
        presenter.onBindRepositoryRowViewAtPosition(holder, position)

    interface Presenter {
        fun onBindRepositoryRowViewAtPosition(holder: AdapterViewHolder, position: Int)
        fun getItemsCount(): Int
        fun getSpanSize(adapterPosition: Int): Int
        fun getItemViewHolder(adapterPosition: Int): ViewHolderType
        fun getTotalItemsOnEachLine(): Int
    }

}
