package com.kinotech.kinotechappv1.ui.feed.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kinotech.kinotechappv1.databinding.FeedNewlistPostBinding
import com.kinotech.kinotechappv1.ui.feed.OnInteractionListener
import com.kinotech.kinotechappv1.ui.feed.PostNewList

class PostNewListAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<PostNewList, PostNewListViewHolder>(PostNewListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostNewListViewHolder {
        val binding =
            FeedNewlistPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostNewListViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostNewListViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}