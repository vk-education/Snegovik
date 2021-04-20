package com.kinotech.kinotechappv1.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.kinotech.kinotechappv1.databinding.FragmentFeedBinding
import com.kinotech.kinotechappv1.ui.feed.recyclerview.PostNewListAdapter

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: FeedViewModel by viewModels()
        val adapter = PostNewListAdapter(object : OnInteractionListener {
            override fun onLike(post: PostNewList) {
                viewModel.likedById(post.id)
            }

            override fun onAdd(post: PostNewList) {
                TODO()
            }
        })

        binding.feedRV.adapter = adapter
        viewModel.newListPosts.observe(viewLifecycleOwner) { post ->
            adapter.submitList(post)
        }

        return binding.root
    }
}

