package com.kinotech.kinotechappv1.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.databinding.FragmentFeedBinding
import com.kinotech.kinotechappv1.ui.feed.recyclerview.PostNewListAdapter
import kotlin.math.log

class FeedFragment : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var posts : ArrayList<PostNewList> = arrayListOf()
    val films : ArrayList<String> = arrayListOf()
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
        }.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                posts.clear()
                for (snap in snapshot.children){
                    user?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Posts")
                            .child(snap.value.toString())
                    }.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (s in snapshot.children){
                                s.getValue(PostNewList::class.java)?.let { posts.add(it) }
                            }
                            Log.d("post", "$posts")
                            binding.feedRV.apply {
                                adapter?.notifyDataSetChanged()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.feedRV.apply {
            adapter = PostNewListAdapter(posts)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
//        user?.uid.let { it1 ->
//            FirebaseDatabase.getInstance().reference
//                .child("Follow")
//                .child(it1.toString())
//                .child("Following")
//        }.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                //posts.clear()
//                for (snap in snapshot.children) {
//                    val openListRef = user?.uid.let { it1 ->
//                        FirebaseDatabase.getInstance().reference
//                            .child("Lists")
//                            .child(snap.value.toString())
//                    }.addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (s in snapshot.children){
//                                Log.d("post", "postPosts ${s.key.toString()} ")
//                                if (snapshot.child(s.key.toString())
//                                        .child("IsOpened").value == true && snapshot.child(s.key.toString())
//                                        .child("Movies").childrenCount >= 3
//                                ) {
//                                    user?.uid.let { it1 ->
//                                        FirebaseDatabase.getInstance().reference
//                                            .child("Lists")
//                                            .child(snap.value.toString())
//                                            .child(snapshot.child(s.key.toString()).key.toString())
//                                            .child("Movies")
//                                    }.addValueEventListener(object : ValueEventListener{
//                                        override fun onDataChange(snapshot: DataSnapshot) {
//                                            for(s3 in snapshot.children){
//                                                films.add(s3.key.toString())
//                                            }
//                                            itemTitle = snapshot.child(s.key.toString()).key.toString()
//                                            user?.uid.let { it1 ->
//                                                FirebaseDatabase.getInstance().reference
//                                                    .child("Users")
//                                                    .child(snap.value.toString())
//                                            }.addValueEventListener(object : ValueEventListener{
//                                                override fun onDataChange(snapshot: DataSnapshot) {
//                                                    fullName = snapshot.child("fullName").value.toString()
//                                                    pic = snapshot.child("photo").value.toString()
//                                                    id = snap.value.toString()
////
//                                                    post = PostNewList(fullName,pic, itemTitle, films[0], films[1], films[2], id)
//                                                    posts.add(post)
//                                                    binding.feedRV.apply {
//                                                        adapter = PostNewListAdapter(posts)
//                                                        setHasFixedSize(true)
//                                                        layoutManager = LinearLayoutManager(context)
//                                                    }
//                                                }
//
//                                                override fun onCancelled(error: DatabaseError) {
//                                                    TODO("Not yet implemented")
//                                                }
//
//                                            })
//
//                                        }
//
//                                        override fun onCancelled(error: DatabaseError) {
//                                            TODO("Not yet implemented")
//                                        }
//
//                                    })
//                                }
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                    })
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })



        return binding.root
    }


}
