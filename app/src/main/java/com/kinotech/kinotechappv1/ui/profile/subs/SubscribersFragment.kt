package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kinotech.kinotechappv1.databinding.SubscribersFragmentBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscribersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscribersFragmentBinding.inflate(inflater, container, false)
        val adapter = SubscribersAdapter()

        binding.subscribersRV.adapter = adapter

        return binding.root
    }
}