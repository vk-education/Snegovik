package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.databinding.SubscriptionItemBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionsViewHolder(
    private val binding: SubscriptionItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subsInfo: SubsInfo) {
        binding.apply {
            profileName.text = subsInfo.fullName
//            profilePic.setImageResource(subsInfo.profilePic)
        }
    }
}