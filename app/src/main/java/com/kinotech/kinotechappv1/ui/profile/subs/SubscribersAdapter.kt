package com.kinotech.kinotechappv1.ui.profile.subs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.SubscribersItemBinding
import com.kinotech.kinotechappv1.db.DatabaseAdder
import com.kinotech.kinotechappv1.ui.profile.SubsInfo
import com.kinotech.kinotechappv1.ui.search.FilmPageFragment
import com.kinotech.kinotechappv1.ui.search.Genres
import com.kinotech.kinotechappv1.ui.search.SimpleResult

class SubscribersAdapter(
    private val subscriber: List<SubsInfo>
) :
    RecyclerView.Adapter<SubscribersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribersViewHolder {
        val binding = SubscribersItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubscribersViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return subscriber.size
    }

    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {
        return holder.bind(subscriber[position])
    }
}

class SubscribersViewHolder(
    private val binding: SubscribersItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subsInfo: SubsInfo) {
        binding.apply {
            profileName.text = subsInfo.fullName
            Glide.with(itemView.context).load(subsInfo.profilePic).into(binding.profilePic)
            //profilePic.setImageResource(subsInfo.profilePic)
//            likeProfile.setImageResource(
//                if (subsInfo.subscribed) R.drawable.ic_liked_40 else R.drawable.ic_like_40dp
//            )

            likeProfile.setOnClickListener {

            }
        }
    }
}

