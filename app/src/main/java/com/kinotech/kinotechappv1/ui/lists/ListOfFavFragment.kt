package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.*
import kotlinx.coroutines.launch

class ListOfFavFragment() : Fragment() {

    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var result: List<SimpleResult>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.list_of_fav_frag, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filmFavListRecyclerView)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Users")

        result = arrayListOf()



        /*val geners: List<Genres> = listOf()
        val countries: List<Countries> = listOf()

        val movieList: List<SimpleResult> = listOf(
            SimpleResult(
                "Побег",
                geners,
                "1111",
                "https://upload.wikimedia.org/wikipedia/ru/c/ce/Green_mile.jpg",
                4,
                countries
            ),
            SimpleResult(
                "Побег",
                geners,
                "1111",
                "https://upload.wikimedia.org/wikipedia/ru/c/ce/Green_mile.jpg",
                4,
                countries
            )
        )*/


        val viewModel = ViewModelProviders.of(this).get(RequestViewModel::class.java)
        lifecycleScope.launch {
            viewModel.getFilms().observe(
                viewLifecycleOwner,
                { result ->
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        Log.d("cout", "response is $result")
                        adapter = MovieFavAdapter(result,context)
                    }
                }
            )
        }
        return root
    }

}