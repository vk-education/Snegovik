package com.kinotech.kinotechappv1.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import kotlinx.coroutines.*

class SearchResultFragment(s: String) : Fragment() {
    private val result = s
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_result, container, false)
        val progressBar: ProgressBar = root.findViewById(R.id.progress_bar)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        val viewModel = ViewModelProviders.of(this).get(RequestViewModel::class.java)
        lifecycleScope.launch {
            viewModel.searchFilms(result)
            viewModel.getFilms().observe(
                viewLifecycleOwner,
                { filmsT ->
                    progressBar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        Log.d("cout", "response is $filmsT")
                        adapter = MoviesAdapter(filmsT, result)
                    }
                }
            )
        }
        return root
    }
}
