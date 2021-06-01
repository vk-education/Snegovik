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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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


class MovieListAdapter(
    private val mData: ArrayList<SimpleResult>,
    val context: Context,
    private val listTitle: String
) :
    RecyclerView.Adapter<MovieListAdapter.MyViewHolder>() {
    var mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = mInflater.inflate(R.layout.list_menu_film, parent, false)
        return MyViewHolder(view, context, listTitle, mData)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(mData[position])
    }

    class MyViewHolder(itemView: View, context: Context, private val listTitle: String, private val mData: ArrayList<SimpleResult>) :
        RecyclerView.ViewHolder(itemView) {
        private val filmPhoto: ImageView = itemView.findViewById(R.id.lmFilmPoster)
        private val filmTitle: TextView = itemView.findViewById(R.id.lmFilmTitle)
        private val filmYear: TextView = itemView.findViewById(R.id.lmFilmYear)
        private val filmGenres: TextView = itemView.findViewById(R.id.lmFilmGenre)
        private var userRating: TextView = itemView.findViewById(R.id.yourRating)
        private val higherDots: ImageView = itemView.findViewById(R.id.lmMenu)
        private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        private val popupMenu: PopupMenu = PopupMenu(context, higherDots)
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
            popupMenu.menuInflater.inflate(R.menu.dot_fav_menu, popupMenu.menu)
            higherDots.setOnClickListener {
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_delete ->
                            user?.uid.let { it1 ->
                                FirebaseDatabase.getInstance().reference
                                    .child("Lists")
                                    .child(it1.toString())
                                    .child(listTitle)
                                    .child("Movies")
                                    .child(movie.filmId.toString())
                                    .removeValue()
                                Toast.makeText(itemView.context, "Удалено", Toast.LENGTH_LONG).show()
                                mData.clear()
                            }
                    }
                    true
                }
                popupMenu.show()
            }
            itemView.setOnClickListener {
                val activity: AppCompatActivity = itemView.context as AppCompatActivity
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, FilmPageFragment(movie, movie.nameRu, 3))
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
                        textView.text = snapshot
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