package com.kinotech.kinotechappv1.db

import android.util.Log
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class DatabaseAdder {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    fun addMovieToDB(movie: SimpleResult, likeButton: ImageButton) {
        user?.let { checkLikedStatus(it.uid, likeButton, movie) }
        likeButton.setOnClickListener {
            if (likeButton.tag == "button_not_liked") {
                user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Liked Movies")
                        .child(it1.toString())
                        .child("Movies")
                        .child(movie.filmId.toString())
                        .setValue(movie)
                }
            } else {
                user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Liked Movies")
                        .child(it1.toString())
                        .child("Movies")
                        .child(movie.filmId.toString())
                        .removeValue()
                }
            }
        }
    }

    private fun checkLikedStatus(uid: String, likeButton: ImageButton, movie: SimpleResult) {
        val likedMoviesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Liked Movies")
                .child(it1.toString())
                .child("Movies")
        }

        likedMoviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(movie.filmId.toString()).exists()) {
                    likeButton.setBackgroundResource(R.drawable.ic_liked)
                    likeButton.tag = "button is liked"
                } else {
                    likeButton.setBackgroundResource(R.drawable.ic_not_liked)
                    likeButton.tag = "button_not_liked"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }

        })
    }
}
