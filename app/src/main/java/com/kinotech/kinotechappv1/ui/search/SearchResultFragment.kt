package com.kinotech.kinotechappv1.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_result, container, false)
        val rootRecyclerViewInflater =
            inflater.inflate(
                R.layout.fragment_search_recycler_view_item_layout,
                container,
                false
            )
        val photoMovie : ImageView = rootRecyclerViewInflater.findViewById(R.id.movie_photo)
        photoMovie.clipToOutline = true
        val request = ServiceBuilder.buildService(APIEndpoints::class.java)
        val progressBar: ProgressBar = root.findViewById(R.id.progress_bar)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        val call = request.findMovies("Побег", "1")
        Log.d("cout", call.toString())
        call.enqueue(
            object : Callback<SearchResults> {
                override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                    Log.d("cout", "onFailure: ")
                    Log.d("cout", "onFailure:$t ")
                }

                override fun onResponse(
                    call: Call<SearchResults>,
                    response: Response<SearchResults>
                ) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        Log.d("cout", "on response")
                        recyclerView.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(context)
                            Log.d("cout", "response is ${response.body()}")
                            Log.d("cout", "before adapter")
                            adapter = MoviesAdapter(response.body()!!.films)
                        }
                    }
                }
            }
        )
        return root
    }
}
