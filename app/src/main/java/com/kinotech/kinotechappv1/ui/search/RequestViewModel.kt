package com.kinotech.kinotechappv1.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class  RequestViewModel : ViewModel() {
    private var searchRequests = SearchRequests()

    suspend fun searchFilms(result: String){
        searchRequests.searchMovie(result)
    }

    suspend fun searchDescriptionRating(movieId: Int){
        searchRequests.searchDescriptionRating(movieId)
    }

    suspend fun searchStaff(movieId: Int){
        searchRequests.searchStaff(movieId)
    }

    fun getFilms():LiveData<List<SimpleResult>>{
        return searchRequests.getFilms()
    }

    fun getDescriptionRatingResults(): LiveData<DescriptionRatingResults> {
        return searchRequests.getDescriptionRating()
    }

    fun getStaffResults(): LiveData<List<Staff>> {
        return searchRequests.getStaff()
    }
}