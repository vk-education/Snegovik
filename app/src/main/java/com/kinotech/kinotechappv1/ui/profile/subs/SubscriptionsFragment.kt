package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.kinotech.kinotechappv1.databinding.SubscripitionsFragmentBinding

class SubscriptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscripitionsFragmentBinding.inflate(inflater, container, false)
        val viewModel: SubscriptionViewModel by viewModels()
        val adapter = SubscriptionsAdapter(
            object : SubsOnInteractionListener {
//                override fun onItem(sub: SubsInfo) {
//                    TODO("Not yet implemented")
//                }

                override fun onAdd(sub: SubsInfo) {
                }
            }
        )

        binding.subscriptionsRV.adapter = adapter
        viewModel.subscription.observe(viewLifecycleOwner) {subs ->
            adapter.submitList(subs)
        }

        return binding.root
    }
}