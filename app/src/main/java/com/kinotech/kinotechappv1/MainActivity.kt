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
    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        createCustomActionBarParams("Лента")
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    @SuppressLint("RestrictedApi")
    private fun createCustomActionBarParams(titleFragment: String) {
        toolbar = supportActionBar!!
        toolbar.show()
        toolbar.setShowHideAnimationEnabled(false)
        toolbar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        toolbar.setCustomView(R.layout.action_bar_title)
        val textView: TextView = toolbar.customView.findViewById(R.id.display_title)
        textView.text = titleFragment
    }

    @SuppressLint("RestrictedApi")
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_feed -> {
                    createCustomActionBarParams(item.title as String)
                    val feedFragment = FeedFragment()
                    openFragment(feedFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    createCustomActionBarParams(item.title as String)
                    val searchFragment = SearchFragment()
                    openFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    createCustomActionBarParams(item.title as String)
                    val listsFragment = ListsFragment()
                    openFragment(listsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    toolbar.hide()
                    supportActionBar!!.setShowHideAnimationEnabled(false)
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
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
