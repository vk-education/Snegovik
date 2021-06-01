package com.kinotech.kinotechappv1.ui.profile.subs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kinotech.kinotechappv1.databinding.SubscribersItemBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscribersAdapter() :
    ListAdapter<SubsInfo, SubscribersViewHolder>(SubsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribersViewHolder {
        val binding = SubscribersItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubscribersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {
        val subscription = getItem(position)
        holder.bind(subscription)
    }
}