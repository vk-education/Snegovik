package com.kinotech.kinotechappv1.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var posts: ArrayList<PostNewList> = arrayListOf()
    var itemTitle = ""
    var fullName = ""
    var pic = ""
    var id = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1.toString())
                .child("Following")
        }.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    posts.clear()
                    for (snap in snapshot.children) {
                        user?.uid.let {
                            FirebaseDatabase.getInstance().reference
                                .child("Posts")
                                .child(snap.value.toString())
                        }.addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (s in snapshot.children) {
                                        s.getValue(PostNewList::class.java)?.let { posts.add(it) }
                                    }
                                    Log.d("post", "$posts")
                                    binding.feedRV.apply {
                                        adapter?.notifyDataSetChanged()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            }
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
        binding.feedRV.apply {
            adapter = PostNewListAdapter(posts)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}
