package com.kinotech.kinotechappv1.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.AuthActivity
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.FragmentProfileBinding
import com.kinotech.kinotechappv1.ui.lists.AnyItemInAdapterList
import com.kinotech.kinotechappv1.ui.lists.MovieListAdapter
import com.kinotech.kinotechappv1.ui.profile.subs.SubsFragment


class ProfileFragment : Fragment() {

    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseUser: FirebaseUser
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var fullName : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.subscribers.setOnClickListener {
            loadSubscribers()
        }
        binding.subscriptions.setOnClickListener {
            loadSubscriptions()
        }
        binding.apply {
            userInfo(textProfile, root, profilePhoto)
            loadRecyclerView(listsRV)
            changeProfileButton.setOnClickListener {
                loadfragment()
            }
        }


        return binding.root
    }

    private fun loadRecyclerView(listsRV: RecyclerView) {
        var list: ArrayList<AnyItemInAdapterList.ButtonShowList> = arrayListOf()
        val listsNamesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")}
        listsNamesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val openedRef = user?.uid.let { it1 ->
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
                                    adapter =  OpenListsAdapter(list, context)
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




    private fun loadfragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, ChangeProfileFragment())
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
        loadfragment()
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

    override fun onResume() {
        super.onResume()
        val buffAcc = GoogleSignIn.getLastSignedInAccount(context)
        //bind(buffAcc)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        binding.imageExit.setOnClickListener {
            mSignInClient.signOut()
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


//    private fun bind(acc: GoogleSignInAccount?) {
//        if (acc == null) {
//            Log.d("check", "null")
//        } else {
//            nickName.text = acc.displayName
//            Glide
//                .with(this)
//                .load(acc.photoUrl)
//                .error(R.drawable.ic_like_40dp)
//                .into(photoAcc)
//        }
//    }
//    private fun userInfo(){
//        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(profileId)
//        usersRef.addValueEventListener(object : ValueEventListener
//        {
//            override fun onDataChange(p0: DataSnapshot){
//                val user = p0.getValue<User>(User::class.java)
//                //Picasso.get().load(user!!.getPhoto()).placeholder(R.drawable.ic_like_40dp).into(photoAcc)
//                view?. = user!!.getFullName()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
//    private fun userInfo()
//    {
//        nickName.text = firebaseUser.displayName
//        Glide
//            .with(this)
//            .load(firebaseUser.photoUrl)
//            .error(R.drawable.ic_like_40dp)
//            .into(photoAcc)
//    }
private fun userInfo(nickName : TextView, v: View, img : ImageView ){
    val usersRef = user?.uid?.let { FirebaseDatabase.getInstance().reference.child("Users").child(it) }
    usersRef?.addValueEventListener(object : ValueEventListener
    {
        override fun onDataChange(p0: DataSnapshot){
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
