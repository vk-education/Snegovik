package com.kinotech.kinotechappv1.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.kinotech.kinotechappv1.R
import com.squareup.picasso.Picasso

class UserAdapter (private var mContext: Context,
                    private var mUser: List<User>,
                    private var isFragment: Boolean = false) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val user = mUser[position]
//        holder.userFullName.text = user.getFullName()
//        Picasso.get().load(user.getPhoto()).into(holder.userPhoto)
//
//        val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
//        pref.putString("uid", user.getUid())
//        pref.apply()
    }

    class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userFullName: TextView = itemView.findViewById(R.id.textProfile)
        var userPhoto: ImageView = itemView.findViewById(R.id.profilePhoto)
        //var followButton = AppCompatButton = itemView.findViewById(R.id.subscribeButton)
    }
}