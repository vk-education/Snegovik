package com.kinotech.kinotechappv1.ui.feed.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
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
import com.kinotech.kinotechappv1.ui.feed.OnInteractionListener
import com.kinotech.kinotechappv1.ui.feed.PostNewList

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
            author.text = postNewList.author
            val options = RequestOptions()
            Glide
                .with(root)
                .load(postNewList.profilePic)
                .apply(options.optionalCircleCrop())
                .error(R.drawable.ic_profile_circle_24)
                .into(profilePic)
            actionDoneTV.text = postNewList.actionDoneText
            setPhotosMovies(postNewList.film1, postNewList.id, postNewList.actionDoneText, root, filmPoster1)
            setPhotosMovies(postNewList.film2, postNewList.id, postNewList.actionDoneText, root, filmPoster2)
            setPhotosMovies(postNewList.film3, postNewList.id, postNewList.actionDoneText, root, filmPoster3)
            //filmPoster1.setImageResource(postNewList.film1)
            //filmPoster2.setImageResource(postNewList.film2)
            //  filmPoster3.setImageResource(postNewList.film3)


        }
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
}



