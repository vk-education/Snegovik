package com.kinotech.kinotechappv1.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.db.DatabaseAdder
import com.kinotech.kinotechappv1.db.SearchQuery


class MoviesAdapter(
    private val movies: List<SimpleResult>,
    private val result: String
) : RecyclerView.Adapter<MoviesViewHolder>() {
    private val s = result
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_recycler_view_item_layout, parent, false)
        Log.d("cout", "in adapter")
        return MoviesViewHolder(view, s)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(movies[position])
    }
}

class MoviesViewHolder(itemView: View, s:String) : RecyclerView.ViewHolder(itemView) {
    private val filmPhoto: ImageView = itemView.findViewById(R.id.movie_photo)
    private val filmTitle: TextView = itemView.findViewById(R.id.movie_title)
    private val filmYear: TextView = itemView.findViewById(R.id.movie_year)
    private val filmGenres: TextView = itemView.findViewById(R.id.movie_genres)
    private val result = s
    val context: Context = itemView.context
    fun bind(movie: SimpleResult) {
        Glide.with(itemView.context).load(movie.posterUrlPreview).into(filmPhoto)
        Log.d("cout", "near bind")
        filmTitle.text = movie.nameRu
        filmYear.text = movie.year
        filmGenres.text = movie.genres.joinToString { genres: Genres ->
            genres.genre
        }
        val likeButton: ImageButton = itemView.findViewById(R.id.like_button_recycler_view)
        val databaseAdder = DatabaseAdder()
        databaseAdder.addMovieToDB(movie, likeButton)
        itemView.setOnClickListener {
            val activity: AppCompatActivity = itemView.context as AppCompatActivity
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, FilmPageFragment(movie, result))
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }



}
