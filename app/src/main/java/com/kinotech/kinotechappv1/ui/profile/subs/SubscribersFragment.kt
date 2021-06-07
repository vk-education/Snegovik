package com.kinotech.kinotechappv1.ui.profile.subs

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
import com.kinotech.kinotechappv1.databinding.SubscribersFragmentBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscribersFragment : Fragment() {

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val result = arrayListOf<SubsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscribersFragmentBinding.inflate(inflater, container, false)
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1.toString())
                .child("Followers")
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    result.clear()
                    for (snap in snapshot.children) {
                        user?.uid.let {
                            FirebaseDatabase.getInstance().reference
                                .child("Users")
                                .child(snap.value.toString())
                        }.addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(snaps: DataSnapshot) {
                                    snaps.getValue(SubsInfo::class.java)?.let { result.add(it) }
                                    Log.d(
                                        "snapInfo",
                                        "onDataChange: ${snaps.getValue(SubsInfo::class.java)}"
                                    )
                                    binding.subscribersRV.apply {
                                        setHasFixedSize(true)
                                        layoutManager = LinearLayoutManager(context)
                                        adapter = SubscribersAdapter(result)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            }
                        )
                        Log.d("followerList", "onDataChange: $result")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
        return binding.root
    }
}
