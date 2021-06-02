package com.kinotech.kinotechappv1.ui.profile.friendssearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SearchUserItemBinding
import com.kinotech.kinotechappv1.AndroidUtils
import com.kinotech.kinotechappv1.ui.profile.FriendProfileFragment
import com.kinotech.kinotechappv1.ui.profile.SubsInfo
import java.util.*

private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

class FriendSearchAdapter(
    private val users: List<SubsInfo>,
    private val subscribeString: String
) : RecyclerView.Adapter<FriendSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchUserItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, subscribeString)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = users[position]
        Log.d("follow button text", "bind: ${users[position]} ")
        return holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(
        private val binding: SearchUserItemBinding,
        private val subscribeString: String
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subsInfo: SubsInfo) {
            binding.apply {
                profileName.text = subsInfo.fullName.split(" ")
                    .joinToString(" ") { it.capitalize(Locale.getDefault()) }

                root.setOnClickListener {
                    AndroidUtils.hideKeyboard(it)
                    val activity: AppCompatActivity = root.context as AppCompatActivity
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.container,
                        FriendProfileFragment(subsInfo)
                    ) // поменять юзера с сабинфо
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                checkFollowingStatus(subsInfo.uid, followBtn)
                followBtn.setOnClickListener {
                    if (followBtn.text.toString() == subscribeString) {
                        firebaseUser?.uid.let { uid ->
                            FirebaseDatabase.getInstance().reference
                                .child("Follow")
                                .child(uid.toString())
                                .child("Following")
                                .child(subsInfo.uid)
                                .setValue(subsInfo.uid)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser?.uid.let { uid ->
                                            FirebaseDatabase.getInstance().reference
                                                .child("Follow")
                                                .child(subsInfo.uid)
                                                .child("Followers")
                                                .child(uid.toString())
                                                .setValue(firebaseUser?.uid)
                                                .addOnCompleteListener { task ->
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
                                .child("Follow")
                                .child(uid.toString())
                                .child("Following")
                                .child(subsInfo.uid)
                                .removeValue()
                                .addOnCompleteListener { task ->
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
                    .child("Follow")
                    .child(it.toString())
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
