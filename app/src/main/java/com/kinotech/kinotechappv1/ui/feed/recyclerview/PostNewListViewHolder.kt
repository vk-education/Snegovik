package com.kinotech.kinotechappv1.ui.feed.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.FeedNewlistPostBinding
import com.kinotech.kinotechappv1.ui.feed.OnInteractionListener
import com.kinotech.kinotechappv1.ui.feed.PostNewList

class PostNewListViewHolder(
    private val binding: FeedNewlistPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(postNewList: PostNewList) {
        binding.apply {
            author.text = postNewList.author
            profilePic.setImageResource(postNewList.profilePic)
            actionDoneTV.text = postNewList.actionDoneText
            likesCount.text = postNewList.likesCount.toString()
            filmPoster1.setImageResource(postNewList.film1)
            filmPoster2.setImageResource(postNewList.film2)
            filmPoster3.setImageResource(postNewList.film3)

            like.setImageResource(
                if (postNewList.likedByMe) R.drawable.ic_liked else R.drawable.ic_not_liked
            )

            like.setOnClickListener {
                onInteractionListener.onLike(postNewList)
            }

        }
    }
}
