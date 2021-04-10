package com.example.githubuserapplication3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapplication3.databinding.ItemRowUserBinding
import com.example.githubuserapplication3.model.FollowerItem

class FollowerListAdapter(private var followerList: ArrayList<FollowerItem>) : RecyclerView.Adapter<FollowerListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ItemRowUserBinding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followerList[position]

        Glide.with(holder.itemView)
                .load(follower.followerAvatar)
                .apply(RequestOptions().override(55, 55))
                .into(holder.binding.itemUserAvatar)
        holder.binding.userUsername.text = follower.followerUsername
    }

    override fun getItemCount(): Int {
        return followerList.size
    }

}