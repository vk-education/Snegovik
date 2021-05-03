package com.kinotech.kinotechappv1.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RequestViewModel : ViewModel() {

    private var descriptionRatingResults: LiveData<DescriptionRatingResults?>? = null
    private var staff: LiveData<List<Staff>>? = null
    private var searchRequests = SearchRequests()


    fun searchDescriptionRating(movieId: Int){
        searchRequests.searchDescriptionRating(movieId)
    }

    fun searchStaff(movieId: Int){
        searchRequests.searchStaff(movieId)
    }

    fun getDescriptionRatingResults(): LiveData<DescriptionRatingResults?>? {
        return descriptionRatingResults
    }

    fun getStaffResults(): LiveData<List<Staff>>? {
        return staff
    }
}