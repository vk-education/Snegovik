package com.kinotech.kinotechappv1.ui.search
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kinotech.kinotechappv1.R
import retrofit2.Call
import retrofit2.Callback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory


class SearchResultFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_result, container, false)
        val request = ServiceBuilder.buildService(APIEndpoints::class.java)
        val progress_bar : ProgressBar = root.findViewById(R.id.progress_bar)
        val recyclerView : RecyclerView = root.findViewById(R.id.recyclerView)
        val call = request.findMovies(getString(R.string.api_key), "Побег")
        call.enqueue(object : Callback<SearchResults> {
            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                Log.d("fail", "onFailure: ")
            }

            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.isSuccessful){
                    progress_bar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = MoviesAdapter(response.body()!!.results)
                    }
                }
            }
        })
        return root
    }
}