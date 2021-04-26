package com.kinotech.kinotechappv1.ui.search

import com.kinotech.kinotechappv1.R
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIEndpoints {

    @GET("v2.1/films/search-by-keyword")
    fun findMovies(@Query("api_key") key:String,
                   @Query("keyword") name:String):
        Call<SearchResults>

}