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
import com.kinotech.kinotechappv1.ui.profile.subs.SubsAdapter
import com.kinotech.kinotechappv1.ui.profile.subs.SubscribersFragment
import com.kinotech.kinotechappv1.ui.profile.subs.SubscriptionsFragment

class SubsFragment : Fragment() {

    private lateinit var binding: SubscribersPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubscribersPageBinding.inflate(inflater, container, false)

        val adapter = SubsAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(SubscribersFragment(), getString(R.string.subscribers))
        adapter.addFragment(SubscriptionsFragment(), getString(R.string.subscriptions))

        binding.viewPager.adapter = adapter

        binding.backBtn.setOnClickListener {
            loadFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.viewPager.adapter = FragmentStateAdapter()
        with(binding) {
            TabLayoutMediator(tabLayout, viewPager) {tab, position ->
                when (position) {
                    0 -> tab.text = "0 подписчиков"
                    1 -> tab.text = "0 подписок"
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
}