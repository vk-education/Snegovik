package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R

class ListsFragment : Fragment() {

    private lateinit var listsViewModel: ListsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_lists, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerview)

        val textView: TextView = root.findViewById(R.id.text_lists)
        listsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )
        return root
    }
}
