package com.kinotech.kinotechappv1.ui.lists

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.FilmPageFragment
import com.kinotech.kinotechappv1.ui.search.Genres
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class MovieFavAdapter(
    private val mData: ArrayList<SimpleResult>,
    val context: Context
) :
    RecyclerView.Adapter<MovieFavAdapter.MyViewHolder>() {
    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = mInflater.inflate(R.layout.list_fav_menu_film, parent, false)
        return MyViewHolder(view, mData)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(mData[position])
    }

    class MyViewHolder(itemView: View, private val mData: ArrayList<SimpleResult>) :
        RecyclerView.ViewHolder(itemView) {

        private val filmPhoto: ImageView = itemView.findViewById(R.id.lmFilmPoster)
        private val filmTitle: TextView = itemView.findViewById(R.id.lmFilmTitle)
        private val filmYear: TextView = itemView.findViewById(R.id.lmFilmYear)
        private val filmGenres: TextView = itemView.findViewById(R.id.lmFilmGenre)
        private var userRating: TextView = itemView.findViewById(R.id.yourRating)
        private val likeButton: ImageButton = itemView.findViewById(R.id.like_in_adapter)
        private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        fun bind(movie: SimpleResult) {
            Log.d("dataFavourite", "bind: ${movie.nameRu}")
            val options = RequestOptions()
            Glide
                .with(itemView.context)
                .load(movie.posterUrlPreview)
                .apply(options.optionalCircleCrop())
                .into(filmPhoto)
            Log.d("count", "near bind")
            filmTitle.text = movie.nameRu
            filmYear.text = movie.year
            filmGenres.text = movie.genres.joinToString { genres: Genres ->
                genres.genre
            }
            checkRating(movie.filmId, userRating)
            addMovieToDB(movie, likeButton)
            itemView.setOnClickListener {
                val activity: AppCompatActivity = itemView.context as AppCompatActivity
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, FilmPageFragment(movie, movie.nameRu, 3))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        private fun addMovieToDB(movie: SimpleResult, likeButton: ImageButton) {
            user?.let { checkLikedStatus(likeButton, movie) }
            likeButton.setOnClickListener {
                if (likeButton.tag == "button_not_liked") {
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Liked Movies")
                            .child(it1.toString())
                            .child("Movies")
                            .child(movie.filmId.toString())
                            .setValue(movie)
                    }
                } else {
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Liked Movies")
                            .child(it1.toString())
                            .child("Movies")
                            .child(movie.filmId.toString())
                            .removeValue()
                    }
                    mData.clear()
                }
            }
        }

        private fun checkLikedStatus(likeButton: ImageButton, movie: SimpleResult) {
            val likedMoviesRef = user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Liked Movies")
                    .child(it1.toString())
                    .child("Movies")
            }

            likedMoviesRef.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(movie.filmId.toString()).exists()) {
                            likeButton.setBackgroundResource(R.drawable.ic_liked)
                            likeButton.tag = "button is liked"
                        } else {
                            likeButton.setBackgroundResource(R.drawable.ic_not_liked)
                            likeButton.tag = "button_not_liked"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("error", "onCancelled: $error")
                    }
                }
            )
        }

        private fun checkRating(id: Int, textView: TextView) {
            val ratingRef = user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("User Rating")
                    .child(it1.toString())
            }
            ratingRef.addValueEventListener(
                object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(id.toString()).exists()) {
                            textView.text = snapshot
                                .child(id.toString())
                                .child("Rating").value as String + "/10"

                            Log.d("rating", "onDataChange:${textView.text} ")
                        } else {
                            textView.text = ""
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        }
    }
}
