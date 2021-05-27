package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kinotech.kinotechappv1.databinding.SubscribersFragmentBinding

class SubscribersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscribersFragmentBinding.inflate(inflater, container, false)
        val viewModel: SubscribersViewModel by viewModels()
        val adapter = SubscribersAdapter(
            object : SubsOnInteractionListener {
//                override fun onItem(sub: SubsInfo) {
//                    viewModel.getSub(sub.id)
//                }

                override fun onAdd(sub: SubsInfo) {
                    viewModel.likedById(sub.id)
                }
            }
        )

        binding.subscribersRV.adapter = adapter
        viewModel.subscribers.observe(viewLifecycleOwner) {subs ->
            adapter.submitList(subs)
        }

        return binding.root
    }
}