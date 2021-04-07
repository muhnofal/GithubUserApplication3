package com.example.githubuserapplication3.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapplication3.CustomOnClickListener
import com.example.githubuserapplication3.Model.Favorite
import com.example.githubuserapplication3.Model.UserItem
import com.example.githubuserapplication3.R
import com.example.githubuserapplication3.UserDetailActivity

class UserListAdapter(private val activity: Activity): RecyclerView.Adapter<UserListAdapter.viewHolder>() {

    var list = ArrayList<UserItem>()

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

        holder.itemView.setOnClickListener(CustomOnClickListener(position, object : CustomOnClickListener.OnItemClickCallback{
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USERNAME, user.username)
//                intent.putExtra(UserDetailActivity.EXTRA_FAVORITE, favorite)
                activity.startActivity(intent)
            }

        }))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAvatar: ImageView = itemView.findViewById(R.id.item_user_avatar)
        var tvUsername: TextView = itemView.findViewById(R.id.user_username)
    }

}