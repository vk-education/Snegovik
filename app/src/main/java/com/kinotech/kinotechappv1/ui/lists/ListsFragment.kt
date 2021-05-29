package com.kinotech.kinotechappv1.ui.lists

import android.graphics.Insets.add
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_lists, container, false)
        recyclerView = root.findViewById(R.id.recyclerview_lists)

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


    override fun onResume() {
        super.onResume()

        context?.let { normalnyContext ->
            recyclerAdapter = RecyclerAdapterLists(normalnyContext, this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = recyclerAdapter
        }
        recyclerAdapter.setMovieListItems(listOfMovie)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onItemClick(item: AnyItemInAdapterList?) {
        Log.d("tag14536", "chek $item")
        when (item) {
            is AnyItemInAdapterList.ButtonCreateList -> {
                val listener: FullNameListener = object : FullNameListener {
                    override fun fullNameEntered(fullName: String) {
                        /*Toast.makeText(
                        context,
                        "Full name: $fullName", Toast.LENGTH_LONG
                        ).show()*/

                        val listsRef = user?.uid.let { it1 ->
                            FirebaseDatabase.getInstance().reference
                                .child("Lists")
                                .child(it1.toString())
                                .child("UserLists")
                                .child(fullName)
                        }

                        listsRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snap in snapshot.children) {
                                    try {
                                        snap.getValue(AnyItemInAdapterList::class.java)
                                            ?.let {
                                                val list = listOfMovie.apply {
                                                    add(2,  AnyItemInAdapterList.ButtonShowList(
                                                        fullName,
                                                        "0 фильмов",
                                                        "https://cdn25.img.ria.ru/images/156087/28/156087280" +
                                                            ".2_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf7" +
                                                            ".50de22f0.jpg"))
                                                }
                                                recyclerAdapter.setMovieListItems(list)
                                                Log.d("list", "onDataChange: $list")
                                                 }
                                    } catch (e: Exception) {
                                        Log.d("oshibka", "onDataChange: $e")
                                        Toast.makeText(context, "Error $e", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }
                                recyclerView.apply {
                                    setHasFixedSize(true)
                                    //layoutManager = LinearLayoutManager(context)
                                   // adapter = RecyclerAdapterLists(context, this@ListsFragment)
                                }
                                //Log.d("dbfav", "onDataChange: $list")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("error", "onCancelled: $error")
                            }
                        })


                        /*val list = listOfMovie.apply {
                            add(
                                2,
                                AnyItemInAdapterList.ButtonShowList(
                                    fullName,
                                    "0 фильмов",
                                    "https://cdn25.img.ria.ru/images/156087/28/156087280" +
                                        ".2_0:778:1536:1642_600x0_80_0_0_606c2d47b6d37951adc9eaf7" +
                                        ".50de22f0.jpg"
                                )
                            )
                        }*/
                        //recyclerAdapter.setMovieListItems(list)
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
                    ?.replace(R.id.fragment_lists, listOfFavFragment, "fragTag")
                    ?.addToBackStack(null)
                    ?.commit()
            }
            is AnyItemInAdapterList.ButtonShowList -> {
                val args = Bundle()
                args.putString("keyForName", item.itemTitle)
                val listOfMovieFragment = ListOfMovieFragment()
                listOfMovieFragment.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_lists, listOfMovieFragment, "fragTag")
                    ?.addToBackStack(null)
                    ?.commit()

            }
        }
    }
}
