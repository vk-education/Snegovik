package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R

class ListsFragment : Fragment() {

    private lateinit var listsViewModel: ListsViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapterLists

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_lists, container, false)

        recyclerView = root.findViewById(R.id.recyclerview_lists)

        /*val textView: TextView = root.findViewById(R.id.text_lists)
        listsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )*/
        return root
    }

    override fun onResume() {
        super.onResume()

        fun generateListOfMovie(): List<ListsOfMovie>{
            return listOf(
                ListsOfMovie(
                "Создать список","сорс","https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
            ), ListsOfMovie(
                    "Понравились", "5 фильмов", "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
            ), ListsOfMovie(
                    "Какое-то название", "7 фильмов", "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                ), ListsOfMovie(
                    "Хорошо похотать", "10 фильмов", "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                )
            )
        }

        /*recyclerAdapter = RecyclerAdapterLists(getContext())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter*/

        getContext()?.let { normalnyContext ->
            recyclerAdapter = RecyclerAdapterLists(normalnyContext)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter

        }
        recyclerAdapter.setMovieListItems(generateListOfMovie())
    }
}
