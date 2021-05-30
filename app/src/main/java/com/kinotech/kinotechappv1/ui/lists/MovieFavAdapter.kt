package com.kinotech.kinotechappv1.ui.lists

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat.apply
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
    private val mData: List<SimpleResult>,
    val context: Context
) :
    RecyclerView.Adapter<MovieFavAdapter.MyViewHolder>() {
    var mInflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = mInflater.inflate(R.layout.list_menu_film, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(mData[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val filmPhoto: ImageView = itemView.findViewById(R.id.lmFilmPoster)
        private val filmTitle: TextView = itemView.findViewById(R.id.lmFilmTitle)
        private val filmYear: TextView = itemView.findViewById(R.id.lmFilmYear)
        private val filmGenres: TextView = itemView.findViewById(R.id.lmFilmGenre)
        private var userRating: TextView = itemView.findViewById(R.id.userRate)
        private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        fun bind(movie: SimpleResult) {
            Log.d("dbfav", "bind: ${movie.nameRu}")
            val options = RequestOptions()
            Glide
                .with(itemView.context)
                .load(movie.posterUrlPreview)
                .apply(options.optionalCircleCrop())
                .into(filmPhoto)
            Log.d("cout", "near bind")
            filmTitle.text = movie.nameRu
            filmYear.text = movie.year
            filmGenres.text = movie.genres.joinToString { genres: Genres ->
                genres.genre
            }
            checkRating(movie.filmId, userRating)
            itemView.setOnClickListener {
                val activity: AppCompatActivity = itemView.context as AppCompatActivity
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.listFavFrag, FilmPageFragment(movie, movie.nameRu, 2))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        private fun checkRating(id: Int, textView: TextView) {
            val ratingRef = user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("User Rating")
                    .child(it1.toString())
            }
            ratingRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(id.toString()).exists()) {
                        textView.text =
                            snapshot
                                .child(id.toString())
                                .child("Rating").value as String + "/10"
                        Log.d("rating", "onDataChange:${textView.text} ")
                    } else {
                        textView.text = ""
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

}