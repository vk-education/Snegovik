package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.lists.CustomDialog.FullNameListener

class ListsFragment : Fragment(), RecyclerAdapterLists.MyClickListener {

    private lateinit var listsViewModel: ListsViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapterLists
    lateinit var buttonOpenDialog: Button

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

    val listOfMovie: ArrayList<AnyItemInAdapterList> = arrayListOf(
        AnyItemInAdapterList.ButtonCreateList(
            "Создать список",
            R.drawable.ic_add_40dp.toString()
        ),
        AnyItemInAdapterList.ButtonShowList(
            "Понравились",
            "5 фильмов",
            R.drawable.ic_like_40dp.toString()
        ),
        AnyItemInAdapterList.ButtonShowList(
            "Какое-то название",
            "7 фильмов",
            "https://upload.wikimedia.org/wikipedia/ru/c/ce/Green_mile.jpg"
        ),
        AnyItemInAdapterList.ButtonShowList(
            "Хорошо похотать",
            "10 фильмов",
            "https://img.gazeta.ru/files3/29/10248029/upload-001-pic905-895x505-11627.jpg"
        )
    )

    override fun onResume() {
        super.onResume()

    /*recyclerAdapter = RecyclerAdapterLists(getContext())
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = recyclerAdapter*/

        context?.let { normalnyContext ->
            recyclerAdapter = RecyclerAdapterLists(normalnyContext, this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        recyclerAdapter.setMovieListItems(listOfMovie)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onItemClick(item: AnyItemInAdapterList?) {
        Log.d("tag14536", "chek $item")
        when (item) {
            is AnyItemInAdapterList.ButtonCreateList -> {
                val listener: FullNameListener = object : FullNameListener {
                    override fun fullNameEntered(fullName: String) {
                    /*Toast.makeText(
                        context,
                        "Full name: $fullName", Toast.LENGTH_LONG
                    ).show()*/
                        val list = listOfMovie.apply {
                            add(
                                2,
                                AnyItemInAdapterList.ButtonShowList(
                                    fullName,
                                    "0 фильмов",
                                    "https://cdn25.img.ria.ru/images/156087/28/156087280" +
                                        ".2_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf7" +
                                        ".50de22f0.jpg"
                                )
                            )
                        }
                        recyclerAdapter.setMovieListItems(list)
                    }
                }
                val dialog = context?.let { CustomDialog(it, listener) }
                dialog?.show()
            }
            is AnyItemInAdapterList.ButtonShowList -> {
            }
        }
    }
}
