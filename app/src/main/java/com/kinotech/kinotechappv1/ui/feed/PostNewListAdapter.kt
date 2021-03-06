package com.kinotech.kinotechappv1.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.FeedNewListPostBinding
import com.kinotech.kinotechappv1.ui.search.SimpleResult
import java.util.Locale
import kotlin.collections.ArrayList

class PostNewListAdapter(private val posts: ArrayList<PostNewList>) :
    RecyclerView.Adapter<PostNewListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostNewListViewHolder {
        val binding = FeedNewListPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostNewListViewHolder(binding, posts)
    }

    override fun onBindViewHolder(holder: PostNewListViewHolder, position: Int) {
        return holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

class PostNewListViewHolder(
    private val binding: FeedNewListPostBinding,
    private val posts: ArrayList<PostNewList>
) : RecyclerView.ViewHolder(binding.root) {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun bind(postNewList: PostNewList) {
        binding.apply {
            author.text = postNewList.fullName.split(" ")
                .joinToString(" ") { it.capitalize(Locale.getDefault()) }
            val options = RequestOptions()
            Glide
                .with(root)
                .load(postNewList.photo)
                .apply(options.optionalCircleCrop())
                .error(R.drawable.ic_profile_circle_24)
                .into(profilePic)
            listName.text = postNewList.actionDoneText
            setPhotosMovies(
                postNewList.films[0],
                postNewList.uid,
                postNewList.actionDoneText,
                root,
                filmPoster1
            )
            setPhotosMovies(
                postNewList.films[1],
                postNewList.uid,
                postNewList.actionDoneText,
                root,
                filmPoster2
            )
            setPhotosMovies(
                postNewList.films[2],
                postNewList.uid,
                postNewList.actionDoneText,
                root,
                filmPoster3
            )
            checkAddedButton(addBtn, postNewList.uid, postNewList.actionDoneText)
            addBtn.setOnClickListener {
                if (addBtn.tag == "button not added") {
                    addList(postNewList.uid, postNewList.actionDoneText)
                } else {
                    deleteList(postNewList.actionDoneText)
                }
            }
            checkLikedStatus(like, postNewList.uid, postNewList.actionDoneText)
            checkLikeCount(likesCount, postNewList.actionDoneText)
            like.setOnClickListener {
                if (like.tag == "button_not_liked") {
                    setLike(postNewList)
                } else {
                    setUnlike(postNewList)
                }
            }
        }
    }

    private fun setLike(postNewList: PostNewList) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Likes")
                .child(postNewList.actionDoneText)
                .child(user?.uid.toString())
                .setValue(true)
        }
    }

    private fun setUnlike(postNewList: PostNewList) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Likes")
                .child(postNewList.actionDoneText)
                .child(user?.uid.toString())
                .removeValue()
        }
    }

    private fun checkLikeCount(count: TextView, title: String) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Likes")
                .child(title)
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    count.text = snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("dbError", "$error")
                }
            }
        )
    }

    private fun checkLikedStatus(likeButton: ImageButton, id: String, title: String) {
        val likedMoviesRef = user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Likes")
                .child(title)
        }

        likedMoviesRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(user?.uid.toString()).exists()) {
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
            }
        )
    }

    private fun setPhotosMovies(
        film: String,
        id: String,
        title: String,
        v: View,
        filmPoster: ImageView
    ) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child(title)
                .child("Movies")
                .child(film)
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Glide
                        .with(v)
                        .load(snapshot.child("posterUrlPreview").value.toString())
                        .error(R.drawable.ic_profile_circle_24)
                        .into(filmPoster)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("dbError", "$error")
                }
            }
        )
    }

    private fun checkAddedButton(addButton: ImageButton, id: String, itemTitle: String) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child("UserLists")
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Lists")
                            .child(it1.toString())
                            .child("UserLists")
                    }.addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snap: DataSnapshot) {
                                if (snap.child(itemTitle).exists()) {
                                    addButton.tag = "button is added"
                                    addButton.setBackgroundResource(R.drawable.ic_check)
                                } else {
                                    addButton.tag = "button not added"
                                    addButton.setBackgroundResource(R.drawable.ic_add)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("dbError", "$error")
                            }
                        }
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("dbError", "$error")
                }
            }
        )
    }

    private fun addList(id: String, itemTitle: String) {
        user?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child(itemTitle)
                .child("Movies")
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Lists")
                            .child(it1.toString())
                            .child("UserLists")
                            .child(itemTitle)
                            .setValue(itemTitle)
                    }
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Lists")
                            .child(it1.toString())
                            .child(itemTitle)
                            .child("IsOpened")
                            .setValue(false)
                    }
                    for (snap in snapshot.children) {
                        val result = snap.getValue(SimpleResult::class.java)
                        user?.uid.let { it1 ->
                            FirebaseDatabase.getInstance().reference
                                .child("Lists")
                                .child(it1.toString())
                                .child(itemTitle)
                                .child("Movies")
                                .child(snap.key.toString())
                                .setValue(result)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("dbError", "$error")
                }
            }
        )
    }

    private fun deleteList(itemTitle: String) {
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(itemTitle)
                .removeValue()
        }
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")
                .child(itemTitle)
                .removeValue()
        }
    }
}
