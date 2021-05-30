package com.kinotech.kinotechappv1.ui.profile.friendssearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SearchUserItemBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

class FriendSearchAdapter(
    private val users: List<SubsInfo>
) : RecyclerView.Adapter<FriendSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchUserItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = users[position]
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(private val binding: SearchUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subsInfo: SubsInfo) {
            binding.apply {

                profileName.text = subsInfo.fullName
                profilePic.setImageResource(subsInfo.profilePic)

                checkFollowingStatus(subsInfo.uid, followBtn)

                followBtn.setOnClickListener {
                    if (followBtn.text.toString() == R.string.subscribe_string.toString()) {
                        firebaseUser?.uid.let { uid ->
                            FirebaseDatabase.getInstance().reference
                                .child("Follow").child(uid.toString())
                                .child("Following").child(subsInfo.uid)
                                .setValue(true).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser?.uid.let { uid ->
                                            FirebaseDatabase.getInstance().reference
                                                .child("Follow").child(subsInfo.uid)
                                                .child("Followers").child(uid.toString())
                                                .setValue(true).addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Log.i("follow", "Подписан")
                                                    }

                                                }

                                        }
                                    }

                                }
                        }
                    } else {
                        firebaseUser?.uid.let { uid ->
                            FirebaseDatabase.getInstance().reference
                                .child("Follow").child(uid.toString())
                                .child("Following").child(subsInfo.uid)
                                .removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser?.uid.let { uid ->
                                            FirebaseDatabase.getInstance().reference
                                                .child("Follow").child(subsInfo.uid)
                                                .child("Followers").child(uid.toString())
                                                .removeValue().addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Log.i("follow", "Отписан")
                                                    }

                                                }

                                        }
                                    }

                                }
                        }


                    }
                }
            }
        }

        private fun checkFollowingStatus(uid: String, followBtn: Button) {
            val followingRef = firebaseUser?.uid.let {
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it.toString())
                    .child("Following")
            }

            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(uid).exists()) {
                        followBtn.setText(R.string.subscribed)
                    } else {
                        followBtn.setText(R.string.subscribe_string)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}