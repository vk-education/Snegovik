package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.lists.CustomDialog.FullNameListener

class ListsFragment : Fragment(), RecyclerAdapterLists.MyClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapterLists
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var listsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var list: ArrayList<AnyItemInAdapterList> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_lists, container, false)
        val activity: AppCompatActivity = root.context as AppCompatActivity
        val toolbar: ActionBar = activity.supportActionBar!!
        toolbar.hide()
        recyclerView = root.findViewById(R.id.recyclerview_lists)

        listsRef = user?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Lists")
                .child(it1.toString())
                .child("UserLists")
        }

        Log.d("list", "we are here: ")
        listsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()
                list.addAll(0, listOfMovie)

                for (snap in snapshot.children) {
                    try {
                        snap.getValue(String::class.java)
                            ?.let {
                                Log.d("lox", "onDataChange: $it")

                                list = list.apply {
                                    add(
                                        2,
                                        AnyItemInAdapterList.ButtonShowList(
                                            it,
                                            "0 фильмов",
                                            ""
                                        )
                                    )
                                }
                            }
                    } catch (e: Exception) {
                        Log.d("error", "onDataChange: $e")
                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                recyclerView.apply {
                    recyclerAdapter.setMovieListItems(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })

        context?.let { normalContext ->
            recyclerAdapter = RecyclerAdapterLists(normalContext, this@ListsFragment)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        recyclerAdapter.setMovieListItems(listOfMovie)

        activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return root
    }

    val listOfMovie: ArrayList<AnyItemInAdapterList> = arrayListOf(
        AnyItemInAdapterList.ButtonCreateList(
            "Создать список",
            R.drawable.ic_add_40dp.toString()
        ),
        AnyItemInAdapterList.ButtonFavList(
            "Понравились",
            "5 фильмов",
            R.drawable.ic_like_40dp.toString()
        )
    )

    override fun onItemClick(item: AnyItemInAdapterList?) {
        Log.d("tag14536", "chek $item")
        when (item) {
            is AnyItemInAdapterList.ButtonCreateList -> {
                val listener: FullNameListener = object : FullNameListener {
                    override fun fullNameEntered(fullName: String) {}
                }
                val dialog = context?.let { CustomDialog(it, listener) }
                dialog?.show()
            }
            is AnyItemInAdapterList.ButtonFavList -> {
                val args = Bundle()
                args.putString("keyForFavName", item.itemTitle)
                val listOfFavFragment = ListOfFavFragment()
                listOfFavFragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, listOfFavFragment, "fragTag")
                    ?.addToBackStack(null)
                    ?.commit()
            }
            is AnyItemInAdapterList.ButtonShowList -> {
                val args = Bundle()
                args.putString("keyForName", item.itemTitle)
                val listOfMovieFragment = ListOfMovieFragment(item.itemTitle)
                listOfMovieFragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, listOfMovieFragment, "fragTag")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }
}
