package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.databinding.SubscribersFragmentBinding
import com.kinotech.kinotechappv1.ui.lists.MovieFavAdapter
import com.kinotech.kinotechappv1.ui.profile.SubsInfo
import com.kinotech.kinotechappv1.ui.profile.User
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class SubscribersFragment : Fragment() {

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var subsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    val result = arrayListOf<SubsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscribersFragmentBinding.inflate(inflater, container, false)
        /*val adapter = SubscribersAdapter(
            object : SubsOnInteractionListener {
                //               override fun onItem(sub: SubsInfo) {
//                    viewModel.getSub(sub.id)
                //               }

                override fun onAdd(sub: SubsInfo) {
//                    viewModel.likedById(sub.id)
                }
            }
        )*/

        subsRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1.toString())
                .child("Followers")
        }
        Log.d("trtrtr", "onCreateView: $subsRef")
        subsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    try {
                        snap.getValue(SubsInfo::class.java)?.let { result.add(it) }
                    } catch (e: Exception) {
                        Log.d("dbfav", "onDataChange: $e")
                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG).show()
                    }
                }
                binding.subscribersRV.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = SubscribersAdapter(result)
                }
                Log.d("dbfav", "onDataChange: $result")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })


        binding.subscribersRV.adapter = SubscribersAdapter(result)
       /* viewModel.subscribers.observe(viewLifecycleOwner) { subs ->
            adapter.submitList(subs)
        }*/

        return binding.root
    }
}