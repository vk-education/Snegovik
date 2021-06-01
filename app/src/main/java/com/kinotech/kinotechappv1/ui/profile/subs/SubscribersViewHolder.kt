package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SubscribersItemBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscribersViewHolder(
    private val binding: SubscribersItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(result: SubsInfo) {
        binding.apply {
            profileName.text = result.fullName
//            profilePic.setImageResource(subsInfo.profilePic)
        }
    }
}