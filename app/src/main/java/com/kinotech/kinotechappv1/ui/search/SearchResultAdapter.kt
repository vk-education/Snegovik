package com.kinotech.kinotechappv1.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinotech.kinotechappv1.R

class MoviesAdapter(private val movies: List<SimpleResult>) : RecyclerView.Adapter<MoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_recycler_view_item_layout, parent, false)
        Log.d("cout", "in adapter")
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(movies[position])
    }
}

class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val filmPhoto: ImageView = itemView.findViewById(R.id.movie_photo)
    private val filmTitle: TextView = itemView.findViewById(R.id.movie_title)
    private val filmYear: TextView = itemView.findViewById(R.id.movie_year)
    private val filmGenres: TextView = itemView.findViewById(R.id.movie_genres)
    fun bind(movie: SimpleResult) {
        Glide.with(itemView.context).load(movie.posterUrlPreview).into(filmPhoto)
        Log.d("cout", "near bind")
        filmTitle.text = movie.nameRu
        filmYear.text = movie.year
        filmGenres.text = movie.genres.joinToString {
            genres: Genres -> genres.genre
        }
    }
}
