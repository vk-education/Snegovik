package com.kinotech.kinotechappv1.ui.profile.subs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kinotech.kinotechappv1.databinding.SubscribersItemBinding
import com.kinotech.kinotechappv1.databinding.SubscriptionItemBinding
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionsAdapter(
    private val subscription: List<SubsInfo>) :
    RecyclerView.Adapter<SubscriptionsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionsViewHolder {
        val binding = SubscriptionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubscriptionsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscription.size
    }

    override fun onBindViewHolder(holder: SubscriptionsViewHolder, position: Int) {
        return holder.bind(subscription[position])
    }
}

class SubscriptionsViewHolder(
    private val binding: SubscriptionItemBinding,
    // private val subsOnInteractionListener: SubsOnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subsInfo: SubsInfo) {
        binding.apply {
            profileName.text = subsInfo.fullName
            Glide.with(itemView.context).load(subsInfo.profilePic).into(binding.profilePic)
            //profilePic.setImageResource(subsInfo.profilePic)
        }
    }
}





