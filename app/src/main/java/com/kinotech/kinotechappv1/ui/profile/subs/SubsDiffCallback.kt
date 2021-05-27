package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.recyclerview.widget.DiffUtil

class SubsDiffCallback : DiffUtil.ItemCallback<SubsInfo>() {
    override fun areItemsTheSame(oldItem: SubsInfo, newItem: SubsInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SubsInfo, newItem: SubsInfo): Boolean {
        return oldItem.subscribed == newItem.subscribed
    }
}