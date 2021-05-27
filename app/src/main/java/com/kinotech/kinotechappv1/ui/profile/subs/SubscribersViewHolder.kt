package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SubscribersItemBinding

class SubscribersViewHolder(
    private val binding: SubscribersItemBinding,
    private val subsOnInteractionListener: SubsOnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subsInfo: SubsInfo) {
        binding.apply {
            profileName.text = subsInfo.name
            profilePic.setImageResource(subsInfo.profilePic)
            likeProfile.setImageResource(
                if (subsInfo.subscribed) R.drawable.ic_liked_40 else R.drawable.ic_like_40dp
            )

            likeProfile.setOnClickListener {
                subsOnInteractionListener.onAdd(subsInfo)
            }
        }
    }
}