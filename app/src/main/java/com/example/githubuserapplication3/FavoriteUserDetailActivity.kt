package com.example.githubuserapplication3

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.githubuserapplication3.Data.ApiService
import com.example.githubuserapplication3.Data.DataRetrofit
import com.example.githubuserapplication3.Model.Favorite
import com.example.githubuserapplication3.Model.UserItem
import com.example.githubuserapplication3.databinding.ActivityFavoriteUserDetailBinding
import com.example.githubuserapplication3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuserapplication3.db.FavoriteHelper
import com.example.githubuserapplication3.helper.MappingHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteUserDetailActivity : AppCompatActivity(){

    var favorite: Favorite? = null
    private lateinit var binding: ActivityFavoriteUserDetailBinding
    private var position: Int = 0
    private lateinit var uriWithId: Uri

    companion object{
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User Detail"

        //get username from FavoriteUserActivity
        favorite = intent.getParcelableExtra(EXTRA_FAVORITE)
        val username = favorite?.username
        if(favorite != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        }else{
            favorite = Favorite()
        }
        //request data
        requestData(username.orEmpty())

        //delete data
        binding.deleteButton.setOnClickListener{
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favorite?.id)
            contentResolver.delete(uriWithId, null, null)
            Toast.makeText(this, "Success to delete", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun requestData(username: String) {
        val apiService = DataRetrofit.getData()?.create(ApiService::class.java)
        apiService?.getUserDetail(username)
            ?.enqueue(object : Callback<UserItem?> {
                override fun onResponse(call: Call<UserItem?>, response: Response<UserItem?>) {
                    val name = response.body()?.name
                    val username = response.body()?.username
                    val company = response.body()?.company
                    val blog = response.body()?.blog
                    val location = response.body()?.location
                    val avatar = response.body()?.image
                    val repository = response.body()?.repos
                    val follower = response.body()?.followers
                    val following = response.body()?.followers

                    binding.let {
                        Glide.with(this@FavoriteUserDetailActivity).load(avatar).into(it.avatar)
                        it.name.text = name
                        it.username.text = username
                        it.company.text = company
                        it.blog.text = blog
                        it.location.text = location
                        it.repository.text = repository.toString()
                        it.follower.text = follower.toString()
                        it.following.text = following.toString()
                    }

                }

                override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        Toast.makeText(this@FavoriteUserDetailActivity, "Check Internet", Toast.LENGTH_SHORT).show()
//                        showLogin(false)
                    }, 2000)
                }
            })
    }

}