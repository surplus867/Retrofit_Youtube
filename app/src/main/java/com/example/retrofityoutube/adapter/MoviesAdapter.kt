package com.example.retrofityoutube.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.retrofityoutube.R
import com.example.retrofityoutube.databinding.ItemMoviesBinding
import com.example.retrofityoutube.response.MoviesListResponse
import com.example.retrofityoutube.utils.Constants.POSTER_BASE_URL
import javax.inject.Inject


class MoviesAdapter @Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMoviesBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root){

        fun set(item : MoviesListResponse.Result) {
            binding.apply {
                tvMovieTItle.text=item.originalTitle
                tvLang.text=item.originalLanguage
                tvRate.text=item.voteAverage.toString()
                tvMovieDateRelease.text=item.releaseDate
                val moviePoster = POSTER_BASE_URL + item.posterPath
                imgMovie.load(moviePoster){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }

    }

    private var onItemClickListener : ((MoviesListResponse.Result) -> Unit)?=null

    fun setOnItemClickListener (listener : (MoviesListResponse.Result) ->Unit){
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<MoviesListResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)
}
