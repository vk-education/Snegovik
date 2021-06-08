package com.kinotech.kinotechappv1.ui.profile

import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendInfoGetter(private val subsInfo: SubsInfo) {
    fun getSubscribers(subscribers: TextView) {
        subsInfo.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1)
                .child("Followers")
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ((snapshot.childrenCount).toString() + "\nподписчики").also {
                        subscribers.text = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    fun getSubscriptions(subscriptions: TextView) {
        subsInfo.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow")
                .child(it1)
                .child("Following")
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ((snapshot.childrenCount).toString() + "\nподписки").also {
                        subscriptions.text = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    fun getListsCount(lists: TextView) {
        subsInfo.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1)
        }.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount - 1 < 0) {
                        (snapshot.childrenCount.toString() + "\nсписки").also { lists.text = it }
                    } else {
                        ((snapshot.childrenCount - 1).toString() + "\nсписки").also {
                            lists.text = it
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}
