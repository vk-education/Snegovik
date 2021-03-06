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
import com.kinotech.kinotechappv1.ui.search.SimpleResult
import java.io.IOException

class ListOfFavFragment : Fragment() {

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.list_of_fav_frag, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filmFavListRecyclerView)
        val btnBack = root.findViewById<ImageButton>(R.id.backFavBtn)
        val result = arrayListOf<SimpleResult>()
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val toolbar: ActionBar = activity.supportActionBar!!
        val listFavTitle = root.findViewById<TextView>(R.id.listFavTitle)
        val arg = this.arguments
        if (arg != null) {
            listFavTitle.text = arg.getString("keyForFavName", "")
        }
        toolbar.hide()
        val likedMoviesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Liked Movies")
                .child(it1.toString())
                .child("Movies")
        }
        likedMoviesRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        try {
                            snap.getValue(SimpleResult::class.java)?.let { result.add(it) }
                        } catch (e: IOException) {
                            Log.d("dataFavourite", "onDataChange: $e")
                            Toast.makeText(context, "Error $e", Toast.LENGTH_LONG).show()
                        }
                    }
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = MovieFavAdapter(result, context)
                    }
                    Log.d("dataFavourite", "onDataChange: $result")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("error", "onCancelled: $error")
                }
            }
        )
        btnBack.setOnClickListener {
            val listsFrag = ListsFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, listsFrag, "fragTag")
                .commit()
        }
        return root
    }
}
