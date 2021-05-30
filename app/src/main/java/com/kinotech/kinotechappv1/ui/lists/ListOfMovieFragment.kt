package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
import com.kinotech.kinotechappv1.ui.search.SearchResultFragment
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class ListOfMovieFragment(private val listTitleDB:String) : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.lists_menu, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filmListRecyclerView)
        val btnBack = root.findViewById<ImageButton>(R.id.backBtn)
        val result = arrayListOf<SimpleResult>()
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val higherDots = root.findViewById<ImageButton>(R.id.higherDots)
        val toolbar: ActionBar = activity.supportActionBar!!
        val listTitle = root.findViewById<TextView>(R.id.listTitle)
        val arg = this.arguments
        if (arg != null) {
            listTitle.text = arg.getString("keyForName", "")
        }
        toolbar.hide()
        val listedMoviesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(listTitleDB)
                .child("Movies")
        }
        listedMoviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    try {
                        snap.getValue(SimpleResult::class.java)?.let { result.add(it) }
                    } catch (e: Exception) {
                        Log.d("dbfav", "onDataChange: $e")
                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG).show()
                    }
                }
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter =  MovieFavAdapter(result, context, listTitleDB,3)
                }
                Log.d("dbfav", "onDataChange: $result")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })

        btnBack.setOnClickListener {
            val listsFrag = ListsFragment()
            activity.supportFragmentManager?.beginTransaction()
                .add(R.id.list_of_movie, listsFrag, "fragTag")
                .commit()
            toolbar.show()
        }

        higherDots.setOnClickListener {
            val popupMenu: PopupMenu? = context?.let { it1 -> PopupMenu(it1, higherDots) }
            popupMenu?.menuInflater?.inflate(R.menu.dot_list_menu, popupMenu.menu)
            popupMenu?.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_share ->
                        Toast.makeText(context, "Ссылка скопирована",
                            Toast.LENGTH_SHORT).show()
                    R.id.item_open ->
                        Toast.makeText(
                            context,
                            "Список виден для других пользователей",
                            Toast.LENGTH_SHORT
                        ).show()
                    R.id.item_delete ->
                        Toast.makeText(
                            context, "Список удален", Toast.LENGTH_SHORT
                        ).show()
                }
                true
            })
            popupMenu?.show()
        }

        return root
    }
}