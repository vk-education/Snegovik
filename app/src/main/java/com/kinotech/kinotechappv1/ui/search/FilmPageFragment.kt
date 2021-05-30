package com.kinotech.kinotechappv1.ui.search

import android.R.attr
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.db.DatabaseAdder
import com.kinotech.kinotechappv1.ui.lists.ListOfFavFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FilmPageFragment(movie: SimpleResult, s: String, mode: Int) : Fragment() {
    private val result = s
    private val movieInfo = movie
    private val movieId = movieInfo.filmId
    private val modeState = mode
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
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
        if (modeState == 1){
            root.isFocusableInTouchMode=true
            root.requestFocus()
            root!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                Log.i(attr.tag.toString(), "keyCode: $keyCode")
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
                    Log.i(attr.tag.toString(), "onKey Back listener is working!!!")
                    fragmentManager?.popBackStack()
                    val fr = SearchResultFragment(result)
                    openFragment(fr)
                    toolbar.show()
                    return@OnKeyListener true
                }
                false
            })
            backButton.setOnClickListener {
                toolbar.show()
                fragmentManager?.popBackStack()
                val fr  = SearchResultFragment(result)
                openFragment(fr)
            }
        }
        else{
            root.isFocusableInTouchMode=true
            root.requestFocus()
            root!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                Log.i(attr.tag.toString(), "keyCode: $keyCode")
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
                    Log.i(attr.tag.toString(), "onKey Back listener is working!!!")
                    fragmentManager?.popBackStack()
                    val favFr = ListOfFavFragment()
                    openFavFragment(favFr)
                    toolbar.hide()
                    return@OnKeyListener true
                }
                false
            })
            backButton.setOnClickListener {
                fragmentManager?.popBackStack()
                toolbar.hide()
                val favFr = ListOfFavFragment()
                openFavFragment(favFr)
            }
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
        val rateMovie :EditText = root.findViewById(R.id.userRate)
        rateMovie.inputType = InputType.TYPE_CLASS_NUMBER
        rateMovie.keyListener = DigitsKeyListener.getInstance("0123456789")
        rateMovie.isSingleLine = true
        checkRating(movieId, rateMovie)
        rateMovie.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        Log.d("rating", "${v?.text} ")
                        user?.uid.let { it1 ->
                            FirebaseDatabase.getInstance().reference
                                .child("User Rating")
                                .child(it1.toString())
                                .child(movieId.toString())
                                .child("Rating")
                                .setValue(v?.text.toString())
                        }
                        return false
                    } else return false
                } else return false
            }

        })
        return root
    }

    private fun checkRating(id: Int, editText : EditText) {
        val ratingRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("User Rating")
                .child(it1.toString())
        }
        ratingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id.toString()).exists()) {
                    editText.hint = snapshot.child(id.toString()).child("Rating").value as String
                    Log.d("rating", "onDataChange:${editText.hint} ")
                } else {
                    editText.hint = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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
        transaction?.commit()
    }
    private fun openFavFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.listFavFrag, fragment)
        transaction?.commit()
    }
}
