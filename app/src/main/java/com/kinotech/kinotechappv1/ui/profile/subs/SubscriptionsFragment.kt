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
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.databinding.SubscripitionsFragmentBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionsFragment : Fragment() {

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val resultSubs = arrayListOf<SubsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscripitionsFragmentBinding.inflate(inflater, container, false)
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1.toString())
                .child("Following")
        }.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultSubs.clear()
                for (snap in snapshot.children) {
                    user?.uid.let {
                        FirebaseDatabase.getInstance().reference
                            .child("Users")
                            .child(snap.value.toString())
                    }.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snaps: DataSnapshot) {
                            snaps.getValue(SubsInfo::class.java)?.let { resultSubs.add(it) }
                            Log.d(
                                "snapInfo",
                                "onDataChange: ${snaps.getValue(SubsInfo::class.java)}"
                            )
                            binding.subscriptionsRV.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(context)
                                adapter = SubscriptionsAdapter(resultSubs)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                    Log.d("followerList", "onDataChange: $resultSubs")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return binding.root
    }
}
