package com.kinotech.kinotechappv1.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.databinding.FragmentFeedBinding
import com.kinotech.kinotechappv1.ui.feed.recyclerview.PostNewListAdapter

class FeedFragment : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var listsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var followingList: ArrayList<String> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModel: FeedViewModel by viewModels()
        val postRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
        }.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val adapter = PostNewListAdapter(
            object : OnInteractionListener {
                override fun onLike(post: PostNewList) {
                    viewModel.likedById(post.id)
                }

//            override fun onAdd(post: PostNewList) {
//                TODO()
//            }
            }
        )

        binding.feedRV.adapter = adapter
        viewModel.newListPosts.observe(viewLifecycleOwner) { post ->
            adapter.submitList(post)
        }

        return binding.root
    }
    private fun checkFollowings(){
        val followingRef = user?.uid?.let {
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it)
                .child("Following")
        }
        followingRef?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    followingList.clear()
                    for (snap in snapshot.children){
                        snapshot.key?.let{ followingList.add(it)}
                    }
                }
                retrievePosts()
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun retrievePosts() {
        val postsRef = user?.uid.let{
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it.toString())
                .child("Following")
        }
    }
}
