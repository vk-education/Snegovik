package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.databinding.SubscriptionItemBinding

class SubscriptionsViewHolder(
    private val binding: SubscriptionItemBinding,
    private val subsOnInteractionListener: SubsOnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subsInfo: SubsInfo) {
        binding.apply {
            profileName.text = subsInfo.name
            profilePic.setImageResource(subsInfo.profilePic)
        }
    }
}