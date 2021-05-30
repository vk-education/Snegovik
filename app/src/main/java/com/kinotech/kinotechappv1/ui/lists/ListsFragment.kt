package com.kinotech.kinotechappv1.ui.lists

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.lists.CustomDialog.FullNameListener

class ListsFragment : Fragment(), RecyclerAdapterLists.MyClickListener {

    private lateinit var listsViewModel: ListsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapterLists
    lateinit var buttonOpenDialog: Button
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var listsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var list: ArrayList<AnyItemInAdapterList> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)
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

        Log.d("list", "mytyt: ")
        listsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()
                list.addAll(0,listOfMovie)

                for (snap in snapshot.children) {
                    try {
                        snap.getValue(String::class.java)
                            ?.let {
                                Log.d("lox", "onDataChange: $it")

                                list = list.apply {
                                    add(2,
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
                    } catch (e: Exception) {
                        Log.d("oshibka", "onDataChange: $e")
                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG)
                            .show()
                    }
                }


                recyclerView.apply {
                    //setHasFixedSize(true)
                    recyclerAdapter.setMovieListItems(list)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "onCancelled: $error")
            }
        })

        context?.let { normalnyContext ->
            recyclerAdapter = RecyclerAdapterLists(normalnyContext, this@ListsFragment)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        recyclerAdapter.setMovieListItems(listOfMovie)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


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

    val listOfMovieCreate: ArrayList<AnyItemInAdapterList> = arrayListOf(
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
                    override fun fullNameEntered(fullName: String) {

                    }
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
