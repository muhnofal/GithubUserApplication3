package com.example.consumeapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumeapp.CustomOnClickListener
import com.example.consumeapp.model.UserItem
import com.example.consumeapp.UserDetailActivity
import com.example.consumeapp.databinding.ItemRowUserBinding

class UserListAdapter(private val activity: Activity): RecyclerView.Adapter<UserListAdapter.viewHolder>() {

    var list = ArrayList<UserItem>()

    class viewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding: ItemRowUserBinding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val user = list[position]

        Glide.with(holder.itemView.context)
            .load(user.image)
            .apply(RequestOptions().override(55,55))
            .into(holder.binding.itemUserAvatar)
        holder.binding.userUsername.text = user.username

        holder.itemView.setOnClickListener(CustomOnClickListener(position, object : CustomOnClickListener.OnItemClickCallback{
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, user)
                activity.startActivity(intent)
            }

        }))

    }

    override fun getItemCount(): Int {
        return list.size
    }



}