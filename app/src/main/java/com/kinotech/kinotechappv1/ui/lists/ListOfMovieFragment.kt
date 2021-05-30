package com.kinotech.kinotechappv1.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.SearchResultFragment
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class ListOfMovieFragment : Fragment() {
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
        val arg = this.arguments
        if (arg != null) {
            listTitle.text = arg.getString("keyForName", "")
        }
        toolbar.hide()

        btnBack.setOnClickListener {
            val listsFrag = ListsFragment();
            activity.supportFragmentManager?.beginTransaction()
                .add(R.id.list_of_movie, listsFrag, "fragTag")
                .addToBackStack(null)
                .commit()
            toolbar.show()
        }

        higherDots.setOnClickListener {
            val popupMenu: PopupMenu? = context?.let { it1 -> PopupMenu(it1, higherDots) }
            popupMenu?.menuInflater?.inflate(R.menu.dot_list_menu, popupMenu.menu)
            popupMenu?.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_share ->
                        Toast.makeText(context, "Ссылка скопирована",
                            Toast.LENGTH_SHORT).show()
                    R.id.item_open ->
                        Toast.makeText(
                            context,
                            "Список виден для других пользователей",
                            Toast.LENGTH_SHORT
                        ).show()
                    R.id.item_delete ->
                        Toast.makeText(
                            context, "Список удален", Toast.LENGTH_SHORT
                        ).show()
                }
                true
            })
            popupMenu?.show()
        }

        return root
    }
}