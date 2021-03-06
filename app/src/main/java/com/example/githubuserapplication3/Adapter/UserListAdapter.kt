package com.example.githubuserapplication3.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapplication3.Model.UserItem
import com.example.githubuserapplication3.R
import com.example.githubuserapplication3.UserDetailActivity

class UserListAdapter(private var list: ArrayList<UserItem>, private val onItemClick: (UserItem) -> Unit): RecyclerView.Adapter<UserListAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val user = list[position]

        Glide.with(holder.itemView.context)
            .load(user.image)
            .apply(RequestOptions().override(55,55))
            .into(holder.ivAvatar)
        holder.tvUsername.text = user.username

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAvatar: ImageView = itemView.findViewById(R.id.item_user_avatar)
        var tvUsername: TextView = itemView.findViewById(R.id.user_username)
    }

}