package com.kinotech.kinotechappv1.ui.profile.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kinotech.kinotechappv1.databinding.SubscripitionsFragmentBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubscripitionsFragmentBinding.inflate(inflater, container, false)
        val adapter = SubscriptionsAdapter()

        binding.subscriptionsRV.adapter = adapter

        return binding.root
    }
}