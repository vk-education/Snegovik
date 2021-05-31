package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.DiffUtil
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubsDiffCallback : DiffUtil.ItemCallback<SubsInfo>() {
    override fun areItemsTheSame(oldItem: SubsInfo, newItem: SubsInfo): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: SubsInfo, newItem: SubsInfo): Boolean {
        return oldItem == newItem
    }
}