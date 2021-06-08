package com.kinotech.kinotechappv1.ui.search

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.kinotech.kinotechappv1.R

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView = root.findViewById<SearchView>(R.id.textView_search)
        val closeBtn: ImageView =
            searchView.findViewById(R.id.search_close_btn)
        val submitBtn: AppCompatButton = root.findViewById(R.id.find_button)
        val outValue = TypedValue()
        context?.resources?.getValue(R.dimen.sixteen, outValue, true)
        val textSizeF = outValue.float
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val toolbar: ActionBar = activity.supportActionBar!!
        toolbar.hide()
        activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        searchView.findViewById<LinearLayout>(R.id.search_plate)
            .setBackgroundColor(Color.TRANSPARENT)
        searchView.findViewById<TextView>(
            R.id.search_src_text
        ).textSize = textSizeF
        searchView.queryHint = getString(R.string.input_film_name)
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    if (query != null) {
                        Log.d("count", "onQueryTextSubmit: $query")
                        openSearchResultFragment(SearchResultFragment(query))
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        submitBtn.setOnClickListener {
                            searchView.clearFocus()
                            openSearchResultFragment(SearchResultFragment(newText))
                        }
                    }
                    return true
                }
            }
        )
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

    private fun openSearchResultFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        transaction?.replace(R.id.frameLayout, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
