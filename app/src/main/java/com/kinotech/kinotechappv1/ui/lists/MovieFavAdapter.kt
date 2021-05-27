package com.kinotech.kinotechappv1.ui.lists

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.db.DatabaseAdder
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
        fun bind(movie: SimpleResult) {
            Log.d("dbfav", "bind: ${movie.nameRu}")
            Glide.with(itemView.context).load(movie.posterUrlPreview).into(filmPhoto)
            Log.d("cout", "near bind")
            filmTitle.text = movie.nameRu
            filmYear.text = movie.year
            filmGenres.text = movie.genres.joinToString { genres: Genres ->
                genres.genre
            }
            val databaseAdder = DatabaseAdder()

            itemView.setOnClickListener {
                /*val activity: AppCompatActivity = itemView.context as AppCompatActivity
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, FilmPageFragment(movie))
                transaction.addToBackStack(null)
                transaction.commit()*/

               /* val activity: AppCompatActivity = itemView.context as AppCompatActivity
                val filmPageFragment = FilmPageFragment();
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.container, filmPageFragment, "fragTag")
                    ?.addToBackStack(null)
                    ?.commit()*/
            }
        }
    }

}