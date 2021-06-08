package com.kinotech.kinotechappv1.ui.profile.friendssearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.FriendsSearchBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo
import java.util.Locale

class FriendsSearchFragment : Fragment() {

    private lateinit var binding: FriendsSearchBinding
    private val users: ArrayList<SubsInfo> = arrayListOf()
    private var adapter: FriendSearchAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FriendsSearchBinding.inflate(inflater, container, false)

        with(binding) {
            searchText.requestFocus()
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val subscribeString = getString(R.string.subscribe_string)
            adapter = context?.let { FriendSearchAdapter(users, subscribeString) }
            Log.d("users", "onCreateView: $users")
            recyclerView.adapter = adapter
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            searchText.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        Log.d("beforeTextChanged", "YES")
                    }

                    override fun onTextChanged(
                        c: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (binding.searchText.text.toString() != "") {
                            recyclerView.visibility = View.VISIBLE
                            retrieveUsers()
                            searchUser(c.toString().toLowerCase(Locale.getDefault()))
                        }
                    }

                    override fun afterTextChanged(c: Editable?) {
                        searchUser(c.toString())
                    }
                }
            )
            searchText.isSingleLine = true
            searchText.setOnEditorActionListener { _, actionId, event ->
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {

                        false
                    } else false
                } else false
            }
        }
        return binding.root
    }

    private fun searchUser(input: String) {
        val userRef = FirebaseDatabase.getInstance().reference
            .child("Users")
            .orderByChild("fullName")
            .startAt(input)
            .endAt(input + "\uf8ff")

        userRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()

                    for (snap in snapshot.children) {
                        val friend = snap.getValue(SubsInfo::class.java)
                        Log.d("friend", "onDataChange: $friend")
                        if (friend != null) {
                            users.add(friend)
                        }
                    }
                    adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("User search", "error: $error")
                }
            }
        )
    }

    private fun retrieveUsers() {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (binding.searchText.text.toString() == "") {
                        users.clear()

                        for (snap in snapshot.children) {
                            val user = snap.getValue(SubsInfo::class.java)
                            if (user != null) {
                                users.add(user)
                            }
                        }
                    }
                    adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Retrieve user", "error: $error")
                }
            }
        )
    }
}
