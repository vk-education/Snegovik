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
import com.kinotech.kinotechappv1.databinding.SubscripitionsFragmentBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionsFragment : Fragment() {

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var subsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    val result = arrayListOf<SubsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscripitionsFragmentBinding.inflate(inflater, container, false)

        subsRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1.toString())
                .child("Following")
        }

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
                binding.subscriptionsRV.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = SubscriptionsAdapter(result)
                }
                Log.d("dbfav", "onDataChange: $result")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })


        binding.subscriptionsRV.adapter = SubscriptionsAdapter(result)
        /* viewModel.subscribers.observe(viewLifecycleOwner) { subs ->
             adapter.submitList(subs)
         }*/

        return binding.root
    }
//    private fun checkFollowings(){
//        val followingRef = user?.uid?.let {
//            FirebaseDatabase.getInstance().reference
//                .child("Follow")
//                .child(it)
//                .child("Following")
//        }
//        followingRef?.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    followingList.clear()
//                    for (snap in snapshot.children){
//                        snapshot.key?.let{ followingList.add(it)}
//                    }
//                }
//                retrievePosts()
//            }
//
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    //}

}