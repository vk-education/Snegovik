package com.kinotech.kinotechappv1.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kinotech.kinotechappv1.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilmPageFragment(id : Int) : Fragment() {
    private val movieId = id
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.film_page, container, false)
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val toolbar: ActionBar = activity.supportActionBar!!
        toolbar.hide()
        val backButton: ImageButton = root.findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            toolbar.show()
            fragmentManager?.popBackStack()
        }
        var staff : List<Staff>
        var descriptionRatingResults : DescriptionRatingResults
        val request = ServiceBuilder.buildService(APIEndpoints::class.java)
        Log.d("cout", "onFailure: $movieId")
        val callStaff = request.findMovieStaff(movieId)
        val progressBar: ProgressBar = root.findViewById(R.id.progress_bar)
        Log.d("cout", "onFailure: $callStaff")
        callStaff.enqueue(
            object : Callback<List<Staff>> {
                override fun onFailure(call: Call<List<Staff>>, t: Throwable) {
                    Log.d("cout", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Staff>>,
                    response: Response<List<Staff>>
                ) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        Log.d("cout", "on response")
                        staff = response.body()!!
                    }
                }
            })

        val filmData = request.findMovieById(movieId, "RATING")
        filmData.enqueue(
            object : Callback<DescriptionRatingResults>{
                override fun onFailure(call: Call<DescriptionRatingResults>, t: Throwable) {
                    Log.d("cout", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<DescriptionRatingResults>,
                    response2: Response<DescriptionRatingResults>
                ) {
                    if (response2.isSuccessful) {
                        progressBar.visibility = View.GONE
                        Log.d("cout", "on response")
                        descriptionRatingResults = response2.body()!!
                        Log.d("cout", "on response2 $descriptionRatingResults")
                    }
                }

            }
        )
        val viewModel = ViewModelProviders.of(this).get(RequestViewModel::class.java)
        Log.d("cout2", "movieId: $movieId")
        viewModel.searchDescriptionRating(movieId)
        viewModel.getDescriptionRatingResults()?.observe(viewLifecycleOwner,
            Observer { descriptionRatingResults_t ->
                val a  =  descriptionRatingResults_t?.data
                Log.d("cout2", "onChanged: $a")
            })
        return root
    }

    private fun getStaff(){

    }
    private fun getFilmData(){

    }
}

