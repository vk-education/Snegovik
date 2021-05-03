package com.kinotech.kinotechappv1.ui.search

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchRequests {
    private val request = ServiceBuilder.buildService(APIEndpoints::class.java)
    private val staff : MutableLiveData<List<Staff>>? = null
    private val descriptionRatingResults : MutableLiveData<DescriptionRatingResults>? = null

    private fun searchRecyclerView(){

    }

    fun searchStaff(movieId : Int){
        val callStaff = request.findMovieStaff(movieId)
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
                        Log.d("cout", "on response")
                        staff?.postValue(response.body()!!)
                    }
                }
            })
    }
    fun searchDescriptionRating(movieId: Int){
        val filmData = request.findMovieById(movieId, "RATING")
        Log.d("cout2", "on response$movieId")
        filmData.enqueue(
            object : Callback<DescriptionRatingResults>{
                override fun onFailure(call: Call<DescriptionRatingResults>, t: Throwable) {
                    Log.d("cout2", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<DescriptionRatingResults>,
                    response2: Response<DescriptionRatingResults>
                ) {
                    if (response2.isSuccessful) {
                        Log.d("cout2", "on response")
                        descriptionRatingResults?.postValue(response2.body()!!)
                        Log.d("cout2", "on response2 $descriptionRatingResults")
                    }
                }

            }
        )
    }

    fun getStaff(): MutableLiveData<List<Staff>>? {
        return staff
    }

    fun getDescriptionRating(): MutableLiveData<DescriptionRatingResults>? {
        return descriptionRatingResults
    }

}