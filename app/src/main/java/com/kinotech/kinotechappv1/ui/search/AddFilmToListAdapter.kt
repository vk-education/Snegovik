package com.kinotech.kinotechappv1.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.lists.AnyItemInAdapterList

class AddFilmToListAdapter(
    private var lists: List<AnyItemInAdapterList> = listOf(),
    private val movie: SimpleResult
) : RecyclerView.Adapter<AddFilmToListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFilmToListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_list, parent, false)
        Log.d("count", "in adapter")
        return AddFilmToListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: AddFilmToListViewHolder, position: Int) {
        return holder.bind(lists[position], movie)
    }
}

class AddFilmToListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val context: Context = itemView.context
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    fun bind(lists: AnyItemInAdapterList, movie: SimpleResult) {
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val filmCount: TextView = itemView.findViewById(R.id.film_count)
        val imgListH: ImageView = itemView.findViewById(R.id.img_list)
        var count: Int
        itemTitle.text = (lists as AnyItemInAdapterList.ButtonShowList).itemTitle
        Log.d("debugging shit", "here 1 ")
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(itemTitle.text.toString())
                .child("Movies")
        }.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count = snapshot.childrenCount.toInt()
                "$count фильмов".also { filmCount.text = it }
                Log.d("img", "onDataChange: $count")
                if (count == 0) {
                    val imgList: String = lists.imgList
                    Glide
                        .with(itemView.context)
                        .load(imgList)
                        .error(R.drawable.ic_baseline_movie_creation_24)
                        .into(imgListH)
                } else {
                    val photoRef = user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Lists")
                            .child(it1.toString())
                            .child(itemTitle.text.toString())
                            .child("Movies")
                    }
                    val queryUid: Query = photoRef.orderByKey().limitToFirst(1)
                    queryUid.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                try {

                                    val result = snap.getValue(SimpleResult::class.java)!!
                                    val imgList: String = result.posterUrlPreview

                                    Glide
                                        .with(itemView.context)
                                        .load(imgList)
                                        .error(R.drawable.ic_baseline_movie_creation_24)
                                        .into(imgListH)
                                } catch (e: Exception) {
                                    Log.d("dataFavourite", "onDataChange: $e")
                                    Toast.makeText(
                                        context, "Error $e", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        itemView.setOnClickListener {
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child(itemTitle.text.toString())
                    .child("Movies")
                    .child(movie.filmId.toString())
                    .setValue(movie)
            }
            val activity: AppCompatActivity = itemView.context as AppCompatActivity
            activity.supportFragmentManager.popBackStack()
        }
    }
}
