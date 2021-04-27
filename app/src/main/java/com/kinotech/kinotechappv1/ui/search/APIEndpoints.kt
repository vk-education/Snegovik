package com.kinotech.kinotechappv1.ui.search

import com.kinotech.kinotechappv1.R
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIEndpoints {
    @Headers("accept:application/json",
    "X-API-KEY: 6b189dca-0b75-4223-9fac-c89f2d2ec5a2")
    @GET("v2.1/films/search-by-keyword")
    fun findMovies(
        @Query("keyword") name: String,
        @Query("page") page: String
    ):
        Call<SearchResults>
}
