package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SubscribersPageBinding
import com.kinotech.kinotechappv1.ui.profile.ProfileFragment
import com.kinotech.kinotechappv1.ui.profile.friendssearch.FriendsSearchFragment

class SubsFragment(private val item: Int) : Fragment() {

    lateinit var binding: SubscribersPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SubscribersPageBinding.inflate(inflater, container, false)

        val adapter = SubsAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(SubscribersFragment(), getString(R.string.subscribers))
        adapter.addFragment(SubscriptionsFragment(), getString(R.string.subscribed))
        val arg = this.arguments

        binding.apply {
            viewPager.adapter = adapter
            viewPager.setCurrentItem(item, false)

            backBtn.setOnClickListener {
                loadFragment()
            }

            search.setOnClickListener {
                loadSearchFragment()
            }

            if (arg != null) {
                profileName.text = arg.getString("keyForNickName", "")
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.subscribers)
                    1 -> tab.text = getString(R.string.subscribed)
                }
            }.attach()
        }
    }

    private fun loadFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, ProfileFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun loadSearchFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, FriendsSearchFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }
}
