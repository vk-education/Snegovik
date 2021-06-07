package com.kinotech.kinotechappv1.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRequests {
    private val request = ServiceBuilder.buildService(APIEndpoints::class.java)
    private val films = MutableLiveData<List<SimpleResult>>()
    private val staff = MutableLiveData<List<Staff>>()
    private val descriptionRatingResults = MutableLiveData<DescriptionRatingResults>()

    suspend fun searchMovie(result: String) = withContext(Dispatchers.IO) {
        val call = request.findMovies(result, "1")
        Log.d("count", call.toString())
        call.enqueue(
            object : Callback<SearchResults> {
                override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                    Log.d("count", "onFailure: ")
                    Log.d("count", "onFailure:$t ")
                }

                override fun onResponse(
                    call: Call<SearchResults>,
                    response: Response<SearchResults>
                ) {
                    if (response.isSuccessful) {
                        Log.d("count", "on response")
                        Log.d("count", "response is ${response.body()}")
                        films.postValue(response.body()!!.films)
                    }
                }
            }
        )
    }

    suspend fun searchStaff(movieId: Int) = withContext(Dispatchers.IO) {
        val callStaff = request.findMovieStaff(movieId)
        callStaff.enqueue(
            object : Callback<List<Staff>> {
                override fun onFailure(call: Call<List<Staff>>, t: Throwable) {
                    Log.d("count", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Staff>>,
                    response: Response<List<Staff>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("count", "on response")
                        staff.postValue(response.body()!!)
                        Log.d("count2", "on response2 $staff")
                    }
                }
            })
    }

    suspend fun searchDescriptionRating(movieId: Int) = withContext(Dispatchers.IO) {
        val filmData = request.findMovieById(movieId, "RATING")
        Log.d("count2", "on response$movieId")
        filmData.enqueue(
            object : Callback<DescriptionRatingResults> {
                override fun onFailure(call: Call<DescriptionRatingResults>, t: Throwable) {
                    Log.d("count2", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<DescriptionRatingResults>,
                    response2: Response<DescriptionRatingResults>
                ) {
                    if (response2.isSuccessful) {
                        Log.d("count2", "on response ${response2.body()!!}")
                        descriptionRatingResults.postValue(response2.body()!!)
                    }
                }
            }
        )
    }

    fun getStaff(): MutableLiveData<List<Staff>> {
        return staff
    }

    fun getDescriptionRating(): MutableLiveData<DescriptionRatingResults> {
        return descriptionRatingResults
    }

    fun getFilms(): MutableLiveData<List<SimpleResult>> {
        return films
    }
}
