package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class ListOfMovieFragment(private val listTitleDB:String) : Fragment() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.lists_menu, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filmListRecyclerView)
        val btnBack = root.findViewById<ImageButton>(R.id.backBtn)
        val result = arrayListOf<SimpleResult>()
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val higherDots = root.findViewById<ImageButton>(R.id.higherDots)
        val toolbar: ActionBar = activity.supportActionBar!!
        val listTitle = root.findViewById<TextView>(R.id.listTitle)
        val listPhoto : ImageView = root.findViewById(R.id.listIcon)
        val arg = this.arguments
        listTitle.text = listTitleDB
        if (arg != null) {
            listTitle.text = arg.getString("keyForName", "")
        }
        toolbar.hide()
        setPhoto(listPhoto, root)
        val listedMoviesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(listTitleDB)
                .child("Movies")
        }
        listedMoviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    try {
                        snap.getValue(SimpleResult::class.java)?.let { result.add(it) }
                    } catch (e: Exception) {
                        Log.d("dbfav", "onDataChange: $e")
                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG).show()
                    }
                }
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter =  MovieListAdapter(result, context, listTitleDB)
                }
                Log.d("dbfav", "onDataChange: $result")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })

        btnBack.setOnClickListener {
            val listsFrag = ListsFragment()
            activity.supportFragmentManager?.beginTransaction()
                .add(R.id.list_of_movie, listsFrag, "fragTag")
                .commit()
        }
        val popupMenu: PopupMenu? = context?.let { it1 -> PopupMenu(it1, higherDots) }
        popupMenu?.menuInflater?.inflate(R.menu.dot_list_menu, popupMenu.menu)
        val itemTitle: MenuItem? = popupMenu?.menu?.getItem(0)
        val listOpenStatusRef = user?.uid.let{it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(listTitleDB)
        }

        listOpenStatusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("IsOpened").value == true) {
                    Log.d("openstatus", "onDataChange: ${itemTitle?.title}")
                    itemTitle?.title = getString(R.string.close_list)
                    Log.d("openstatus", "onDataChange: ${itemTitle?.title}")
                } else {
                    itemTitle?.title = getString(R.string.open_list)
                }
                higherDots.setOnClickListener {
                    popupMenu?.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.item_open ->
                                setOpenList(item)
                            R.id.item_delete ->
                                deleteList()
                        }
                        true
                    }
                    popupMenu?.show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })
        return root
    }
    private fun setOpenList(item: MenuItem?){
        if (item?.title == getString(R.string.open_list)){
            Toast.makeText(
                context,
                "Список виден для других пользователей",
                Toast.LENGTH_SHORT
            ).show()
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child(listTitleDB)
                    .child("IsOpened")
                    .setValue(true)
            }
            val userInfo : HashMap<String, Any?> = HashMap()
            val films : ArrayList<String> = arrayListOf()

            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child(listTitleDB)
                    .child("Movies")
            }.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount >= 3){
                        for(snap in snapshot.children){
                            films.add(snap.key.toString())
                        }
                        user?.uid.let { it1 ->
                            FirebaseDatabase.getInstance().reference
                                .child("Users")
                                .child(it1.toString())
                        }.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                userInfo["uid"] = snapshot.child("uid").value
                                userInfo["fullName"] = snapshot.child("fullName").value
                                userInfo["photo"] = snapshot.child("photo").value
                                userInfo["actionDoneText"] = listTitleDB
                                userInfo["films"] = films
                                user?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Posts")
                                        .child(it1.toString())
                                        .child(listTitleDB)
                                        .setValue(userInfo)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
        else{
            Toast.makeText(
                context,
                "Список закрыт для остальных пользователей",
                Toast.LENGTH_SHORT
            ).show()
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child(listTitleDB)
                    .child("IsOpened")
                    .setValue(false)
            }
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Posts")
                    .child(it1.toString())
                    .removeValue()
            }

        }
    }

    private fun deleteList(){
        Toast.makeText(
            context, "Список удален", Toast.LENGTH_SHORT
        ).show()
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(listTitleDB)
                .removeValue()
        }
        user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")
                .child(listTitleDB)
                .removeValue()
        }
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.container, ListsFragment())
            ?.commit()
    }

    private fun setPhoto(img : ImageView, v : View){
        val defaultPhoto = "https://cdn25.img.ria.ru/images/156087/28/156087280" +
        ".2_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf7" +
            ".50de22f0.jpg"
        val listedMoviesRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child(listTitleDB)
                .child("Movies")
        }
        listedMoviesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toInt() == 0) {
                    Glide
                        // .with(itemView.context)
                        .with(v.context)
                        .load(defaultPhoto)
                        .error(R.drawable.ic_baseline_movie_creation_24)
                        .into(img)
                } else {
                    val queryUid: Query = listedMoviesRef.orderByKey().limitToFirst(1)
                    queryUid.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                try {
                                    val result = snap.getValue(SimpleResult::class.java)!!
                                    val imgList: String = result.posterUrlPreview
                                    Log.d(
                                        "dbImg",
                                        "onDataChange3:${snapshot.childrenCount.toInt()} "
                                    )
                                    Glide
                                        .with(v.context)
                                        .load(imgList)
                                        .error(R.drawable.ic_baseline_movie_creation_24)
                                        .into(img)

                                } catch (e: Exception) {
                                    Log.d("dbfav", "onDataChange: $e")
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
