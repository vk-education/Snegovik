package com.kinotech.kinotechappv1.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kinotech.kinotechappv1.R

class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchView: SearchView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView = root.findViewById<SearchView>(R.id.textView_search)
        val closeBtn: ImageView =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val submitBtn: AppCompatButton = root.findViewById(R.id.find_button)
        searchView.findViewById<LinearLayout>(androidx.appcompat.R.id.search_plate)
            .setBackgroundColor(Color.TRANSPARENT)
        searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text).textSize = 16F
        searchView.queryHint = getString(R.string.input_film_name)
        submitBtn.setOnClickListener {
            searchView.clearFocus()
            openSearchResultFragment(SearchResultFragment())
        }
        closeBtn.setOnClickListener {
            if (searchView.query.isEmpty()) {
                searchView.isIconified = true
            } else {
                searchView.setQuery("", false)
                searchView.clearFocus()
            }
        }
        return root
    }

    private fun onQueryTextSubmit(): Boolean {
        searchView.clearFocus()
        openSearchResultFragment(SearchResultFragment())
        return true
    }

    private fun openSearchResultFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout, fragment)
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }
}
