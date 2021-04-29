package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R

class ListsFragment : Fragment(), RecyclerAdapterLists.MyClickListener {

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

        fun generateListOfMovie(): List<AnyItemInAdapterList> {
            return listOf(
                AnyItemInAdapterList.ButtonCreateList(
                    "Создать список",
                    "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:" +
                        ".1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                ),
                AnyItemInAdapterList.ButtonShowList(
                    "Понравились",
                    "5 фильмов",
                    "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:153" +
                        ".6:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                ),
                AnyItemInAdapterList.ButtonShowList(
                    "Какое-то название",
                    "7 фильмов",
                    "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:164" +
                        ".2_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                ),
                AnyItemInAdapterList.ButtonShowList(
                    "Хорошо похотать",
                    "10 фильмов",
                    "https://cdn25.img.ria.ru/images/156087/28/1560872802_0:778:1536:1" +
                        ".642_600x0_80_0_0_606c2d47b6d37951adc9eaf750de22f0.jpg"
                )
            )
        }

        /*recyclerAdapter = RecyclerAdapterLists(getContext())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter*/

        context?.let { normalnyContext ->
            recyclerAdapter = RecyclerAdapterLists(normalnyContext,this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        recyclerAdapter.setMovieListItems(generateListOfMovie())


    }

    override fun onItemClick(item: AnyItemInAdapterList?) {
        Log.d("tag14536", "chek $item")
    }

}
