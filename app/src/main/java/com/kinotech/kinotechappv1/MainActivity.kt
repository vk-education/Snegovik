package com.kinotech.kinotechappv1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kinotech.kinotechappv1.ui.feed.FeedFragment
import com.kinotech.kinotechappv1.ui.lists.ListsFragment
import com.kinotech.kinotechappv1.ui.profile.ProfileFragment
import com.kinotech.kinotechappv1.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val toolbar: ActionBar = supportActionBar!!
        toolbar.hide()
        navView.setupWithNavController(navController)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    @SuppressLint("RestrictedApi")
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_feed -> {
                    val feedFragment = FeedFragment()
                    openFragment(feedFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    val searchFragment = SearchFragment()
                    openFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_lists -> {
                    val listsFragment = ListsFragment()
                    openFragment(listsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    val profileFragment = ProfileFragment()
                    openFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

}
