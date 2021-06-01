package com.kinotech.kinotechappv1.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.FriendsProfileBinding
import com.kinotech.kinotechappv1.ui.lists.AnyItemInAdapterList
import com.kinotech.kinotechappv1.ui.profile.friendssearch.FriendsSearchFragment
import com.kinotech.kinotechappv1.ui.profile.subs.SubsFragment

class FriendProfileFragment(private val subsInfo: SubsInfo) : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FriendsProfileBinding
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FriendsProfileBinding.inflate(inflater, container, false)
        user = FirebaseAuth.getInstance().currentUser!!
        binding.friendSubscribers.setOnClickListener {
            loadSubscribers()
        }
        binding.friendSubscriptions.setOnClickListener {
            loadSubscriptions()
        }
        binding.apply {
            userInfo(friendTextProfile, root, friendPhoto)

            backBtnCh.setOnClickListener {
                loadFragment()
            }

            checkFollowingStatus(subscribeButton)
            subscribeButton.setOnClickListener {
                if (subscribeButton.text.toString() == "Подписаться") { // Расхардкодить
                    user.uid.let { uid ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(uid)
                            .child("Following").child(subsInfo.uid)
                            .setValue(null).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    user.uid.let { uid ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(subsInfo.uid)
                                            .child("Followers").child(uid)
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
                    user.uid.let { uid ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(uid)
                            .child("Following").child(subsInfo.uid)
                            .removeValue().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    user.uid.let { uid ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(subsInfo.uid)
                                            .child("Followers").child(uid)
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
//            loadRecyclerView(listsRV)
        }
        return binding.root
    }
    private fun loadRecyclerView(listsRV: RecyclerView) {
        var list: ArrayList<AnyItemInAdapterList.ButtonShowList> = arrayListOf()
        val listsNamesRef = subsInfo.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")}
        listsNamesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val openedRef = subsInfo.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Lists")
                            .child(it1.toString())
                            .child(snap.value.toString())
                            .child("IsOpened")
                    }
                    openedRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value == true){
                                snap.getValue(String::class.java)
                                    ?.let {
                                        Log.d("lox", "onDataChange: $it")
                                        list = list.apply {
                                            add(
                                                AnyItemInAdapterList.ButtonShowList(
                                                    it,
                                                    "0 фильмов",
                                                    "https://cdn25.img.ria.ru/images/156087/28/156087280" +
                                                        ".2_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf7" +
                                                        ".50de22f0.jpg"
                                                )
                                            )
                                        }
                                    }
                                listsRV.apply {
                                    setHasFixedSize(true)
                                    layoutManager = LinearLayoutManager(context)
                                    adapter =  OpenListsFriendsAdapter(list, context)
                                }
                            }
                        }


                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }

                    })
                }
                Log.d("profileRecycler", "$list")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }

        })
        listsRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter =  OpenListsAdapter(list, context)
        }
    }

    private fun checkFollowingStatus(followBtn: Button) {
        val followingRef = user.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it)
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(subsInfo.uid).exists()) {
                    followBtn.setText(R.string.subscribed)
                } else {
                    followBtn.setText(R.string.subscribe_string)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, FriendsSearchFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun loadSubscribers() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, SubsFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun loadSubscriptions() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(
                R.id.container,
                SubsFragment()
            ) // Поменять на второй лист SubsFragment
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun userInfo(nickName: TextView, v: View, img: ImageView) {
        val usersRef = subsInfo.uid.let {
            FirebaseDatabase.getInstance().reference.child("Users").child(it)
        }
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                nickName.text = p0.child("fullName").value.toString()
                Glide
                    .with(v.context)
                    .load(p0.child("photo").value.toString())
                    .into(img)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
