package com.kinotech.kinotechappv1.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.lists.AnyItemInAdapterList

class AddFilmToListFragment(
    private val movie: SimpleResult
) : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var list: ArrayList<AnyItemInAdapterList> = arrayListOf()
    lateinit var recyclerAdapter: AddFilmToListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("debugging shit", "here 1 ")
        val root = inflater.inflate(R.layout.fragment_add_film_to_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerview_lists_film_page)
        val listsRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")
        }
        root.isFocusableInTouchMode = true
        root.requestFocus()
        Log.d(android.R.attr.tag.toString(), "keyCode:")
        root!!.setOnKeyListener(
            View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    fragmentManager?.popBackStack()
                    val fr = FilmPageFragment(movie, movie.nameRu, 2)
                    openFragment(fr)
                    return@OnKeyListener true
                }
                false
            }
        )
        listsRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snap in snapshot.children) {
                        try {
                            snap.getValue(String::class.java)
                                ?.let {
                                    Log.d("lox", "onDataChange: $it")

                                    list = list.apply {
                                        add(
                                            AnyItemInAdapterList.ButtonShowList(
                                                it,
                                                "0 фильмов",
                                                ""
                                            )
                                        )
                                    }
                                }
                        } catch (e: Exception) {
                            Log.d("error", "onDataChange: $e")
                            Toast.makeText(context, "Error $e", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    recyclerView.apply {
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerAdapter = AddFilmToListAdapter(list, movie)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("error", "onCancelled: $error")
                }
            }
        )

        context?.let {
            recyclerAdapter = AddFilmToListAdapter(list, movie)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        return root
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, fragment)
        transaction?.commit()
    }
}
