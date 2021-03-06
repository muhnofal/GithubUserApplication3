package com.example.githubuserapplication3.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapplication3.Model.FollowingItem
import com.example.githubuserapplication3.R
import com.example.githubuserapplication3.databinding.ItemRowFollowingBinding

class FollowingListAdapter(val followingList: ArrayList<FollowingItem>): RecyclerView.Adapter<FollowingListAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemRowFollowingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ItemRowFollowingBinding = ItemRowFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val following = followingList[position]

        Glide.with(holder.itemView)
            .load(following.followingAvatar)
            .apply(RequestOptions().override(55,55))
            .into(holder.binding.itemFollowingAvatar)
        holder.binding.followingUsername.text = following.followingUsername
    }

    override fun getItemCount(): Int {
        return followingList.size
    }

}