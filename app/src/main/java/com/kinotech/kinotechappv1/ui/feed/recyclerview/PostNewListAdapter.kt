package com.kinotech.kinotechappv1.ui.feed.recyclerview

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
import com.kinotech.kinotechappv1.databinding.FeedNewlistPostBinding
import com.kinotech.kinotechappv1.ui.feed.PostNewList
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class PostNewListAdapter(private val posts : ArrayList<PostNewList>) :
    RecyclerView.Adapter<PostNewListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostNewListViewHolder {
        val binding = FeedNewlistPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostNewListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostNewListViewHolder, position: Int) {
        return holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

class PostNewListViewHolder(
    private val binding: FeedNewlistPostBinding
) : RecyclerView.ViewHolder(binding.root) {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun bind(postNewList: PostNewList) {
        binding.apply {
            author.text = postNewList.fullName
            val options = RequestOptions()
            Glide
                .with(root)
                .load(postNewList.photo)
                .apply(options.optionalCircleCrop())
                .error(R.drawable.ic_profile_circle_24)
                .into(profilePic)
            actionDoneTV.text = postNewList.actionDoneText
            val films = postNewList.films
           setPhotosMovies(postNewList.films[0], postNewList.uid, postNewList.actionDoneText, root, filmPoster1)
           setPhotosMovies(postNewList.films[1], postNewList.uid, postNewList.actionDoneText, root, filmPoster2)
           setPhotosMovies(postNewList.films[2], postNewList.uid, postNewList.actionDoneText, root, filmPoster3)
            checkAddedButton(addBtn, postNewList.uid, postNewList.actionDoneText )
            addBtn.setOnClickListener {
                if (addBtn.tag == "button not added") {
                    addList(postNewList.uid, postNewList.actionDoneText)
                } else {
                    deleteList(postNewList.actionDoneText)
                }
            }
            checkLikedStatus(like, postNewList.uid)
            checkLikeCount(postNewList.uid, likesCount)
            like.setOnClickListener {
                if (like.tag == "button_not_liked"){
                    user?.uid.let{it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Posts")
                            .child(postNewList.uid)
                            .child("Likes")
                            .child(postNewList.uid)
                            .setValue(true)
                    }
                }
                else{
                    user?.uid.let{it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Posts")
                            .child(postNewList.uid)
                            .child("Likes")
                            .child(postNewList.uid)
                            .removeValue()
                    }
                }

            }
            //filmPoster1.setImageResource(postNewList.film1)
            //filmPoster2.setImageResource(postNewList.film2)
            //  filmPoster3.setImageResource(postNewList.film3)


        }
    }

    private fun checkLikeCount(id:String, count: TextView) {
        user?.uid.let{it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Posts")
                .child(id)
                .child("Likes")
        }.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkLikedStatus(likeButton: ImageButton, id: String) {
        val likedMoviesRef = user?.uid.let{it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Posts")
                .child(id)
                .child("Likes")
        }

        likedMoviesRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id).exists()){
                    likeButton.setBackgroundResource(R.drawable.ic_liked)
                    likeButton.tag = "button is liked"
                }
                else{
                    likeButton.setBackgroundResource(R.drawable.ic_not_liked)
                    likeButton.tag = "button_not_liked"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }

        })
    }

    private fun setPhotosMovies(film:String, id:String, title:String, v: View, filmPoster : ImageView) {
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child(title)
                .child("Movies")
                .child(film)
        }.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Glide
                    .with(v)
                    .load(snapshot.child("posterUrlPreview").value.toString())
                    .error(R.drawable.ic_profile_circle_24)
                    .into(filmPoster)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun checkAddedButton(addButton: ImageButton, id:String, itemTitle:String) {
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child("UserLists")
        }.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Lists")
                        .child(it1.toString())
                        .child("UserLists")
                }.addValueEventListener(object : ValueEventListener {
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
                        TODO("Not yet implemented")
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addList(id: String, itemTitle: String) {
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(id)
                .child(itemTitle)
                .child("Movies")
        }.addValueEventListener(object : ValueEventListener {
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
                TODO("Not yet implemented")
            }

        })
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



