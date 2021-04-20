package com.kinotech.kinotechappv1.ui.feed.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.kinotech.kinotechappv1.ui.feed.PostNewList

class PostNewListDiffCallback : DiffUtil.ItemCallback<PostNewList>() {
    override fun areItemsTheSame(oldItem: PostNewList, newItem: PostNewList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostNewList, newItem: PostNewList): Boolean {
        return oldItem == newItem
    }
}