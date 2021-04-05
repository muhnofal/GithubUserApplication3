package com.example.githubuserapplication3.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapplication3.CustomOnClickListener
import com.example.githubuserapplication3.FavoriteUserDetailActivity
import com.example.githubuserapplication3.databinding.ItemRowFavoriteBinding
import com.example.githubuserapplication3.Model.Favorite

class FavoriteListAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    var listFavorites = ArrayList<Favorite>()

    set(listFavorites) {
        if (listFavorites.size > 0){
            this.listFavorites.clear()
        }
        this.listFavorites.addAll(listFavorites)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRowFavoriteBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRowFavoriteBinding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(listFavorites[position])
        val favorite = listFavorites[position]


        Glide.with(holder.itemView)
                .load(favorite.avatar)
                .apply(RequestOptions().override(55,55))
                .into(holder.binding.favoriteAvatar)
        holder.binding.favoriteUsername.text = favorite.username
        holder.binding.favoriteClick.setOnClickListener(CustomOnClickListener(position, object : CustomOnClickListener.OnItemClickCallback{
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(activity, FavoriteUserDetailActivity::class.java)
                intent.putExtra(FavoriteUserDetailActivity.EXTRA_FAVORITE, favorite)
                activity.startActivity(intent)
            }

        }))

    }

    override fun getItemCount(): Int {
         return this.listFavorites.size
    }

}