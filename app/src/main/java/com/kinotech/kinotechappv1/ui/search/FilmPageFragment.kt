package com.kinotech.kinotechappv1.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.db.DatabaseAdder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmPageFragment(movie: SimpleResult, s:String) : Fragment() {
    private val result = s
    private val movieInfo = movie
    private val movieId = movieInfo.filmId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.film_page, container, false)
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val toolbar: ActionBar = activity.supportActionBar!!
        toolbar.hide()
        val progressBar: ProgressBar = root.findViewById(R.id.progress_bar)
        val backButton: ImageButton = root.findViewById(R.id.backBtn)
        val viewModel = ViewModelProviders.of(this).get(RequestViewModel::class.java)
        backButton.setOnClickListener {
            toolbar.show()
            fragmentManager?.popBackStack()
            val fr  = SearchResultFragment(result)
            openFragment(fr)
        }

        Log.d("cout2", "movieId: $movieId")
        lifecycleScope.launch {
            viewModel.searchDescriptionRating(movieId)
            viewModel.getDescriptionRatingResults().observe(
                viewLifecycleOwner,
                { descriptionRatingResults_t ->
                    val description = descriptionRatingResults_t?.data?.description
                    val ratingKP = descriptionRatingResults_t?.rating?.rating
                    val ratingImdb = descriptionRatingResults_t?.rating?.ratingImdb
                    lifecycleScope.launch {
                        if (description != null) {
                            setDescriptionRating(
                                root,
                                ratingKP.toString(),
                                ratingImdb.toString(),
                                description
                            )
                        }
                    }
                }
            )

            viewModel.searchStaff(movieId)
            viewModel.getStaffResults().observe(
                viewLifecycleOwner,
                { staffT ->
                    val directors = ArrayList<String>()
                    val actors = ArrayList<String>()
                    getStaff(staffT, directors, actors)
                    lifecycleScope.launch {
                        setStaff(root, directors, actors)
                    }
                    Log.d("cout2", "$actors")
                }
            )
        }
        lifecycleScope.launch {
            setMovieData(root)
        }
        progressBar.visibility = View.GONE
        val likeButton:ImageButton = root.findViewById(R.id.likeFilm)
        val databaseAdder = DatabaseAdder()
        databaseAdder.addMovieToDB(movieInfo, likeButton)
        return root
    }


    private fun getStaff(
        staff: List<Staff>,
        directors: ArrayList<String>,
        actors: ArrayList<String>
    ) {
        var directorsCount = 0
        var actorsCount = 0
        for (item in staff) {
            if (item.professionKey == "DIRECTOR") {
                directorsCount++
                if (directorsCount == 4)
                    continue
                directors.add(item.nameRu)
            } else if (item.professionKey == "ACTOR") {
                actorsCount++
                if (actorsCount == 4)
                    break
                actors.add(item.nameRu)
            }
        }
    }

    private suspend fun setDescriptionRating(
        movieView: View,
        ratingKP: String,
        ratingImdb: String,
        description: String
    ) = withContext(Dispatchers.Main) {
        val filmRatingKP: TextView = movieView.findViewById(R.id.kpRanking)
        val filmRatingImdb: TextView = movieView.findViewById(R.id.imdbRanking)
        val filmDescription: TextView = movieView.findViewById(R.id.filmDescription)
        filmRatingKP.text = ratingKP
        filmRatingImdb.text = ratingImdb
        filmDescription.text = description
    }

    private suspend fun setStaff(
        movieView: View,
        directors: ArrayList<String>,
        actors: ArrayList<String>
    ) = withContext(Dispatchers.Main) {
        val filmDirectors: TextView = movieView.findViewById(R.id.directorName)
        val filmActors: TextView = movieView.findViewById(R.id.rolesList)
        filmDirectors.text = directors.joinToString(", ")
        filmActors.text = actors.joinToString(", ")
    }

    private suspend fun setMovieData(movieView: View) = withContext(Dispatchers.Main) {
        val filmPhoto: ImageView = movieView.findViewById(R.id.filmSearchPoster)
        val filmTitle: TextView = movieView.findViewById(R.id.filmTitle)
        val filmYearCountry: TextView = movieView.findViewById(R.id.whenWhere)
        val filmGenres: TextView = movieView.findViewById(R.id.genre)
        val countries = movieInfo.countries.joinToString { countries: Countries ->
            countries.country
        }
        val yearCountry: String = arrayListOf(movieInfo.year, countries).joinToString(", ")
        Log.d("cout", "near setting")
        Glide.with(movieView.context).load(movieInfo.posterUrlPreview).into(filmPhoto)
        filmTitle.text = movieInfo.nameRu
        filmYearCountry.text = yearCountry
        filmGenres.text = movieInfo.genres.joinToString { genres: Genres ->
            genres.genre
        }
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
