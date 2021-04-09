package com.example.githubuserapplication3

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.githubuserapplication3.data.ApiService
import com.example.githubuserapplication3.data.DataRetrofit
import com.example.githubuserapplication3.model.Favorite
import com.example.githubuserapplication3.model.UserItem
import com.example.githubuserapplication3.databinding.ActivityFavoriteUserDetailBinding
import com.example.githubuserapplication3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
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

        showLoading(true)

        //request data
        requestData(username.orEmpty())

        //delete data
        binding.deleteButton.setOnClickListener{
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favorite?.id)
            contentResolver.delete(uriWithId, null, null)
            Toast.makeText(this, resources.getString(R.string.succes_delete), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun requestData(username: String) {
        val apiService = DataRetrofit.getData()?.create(ApiService::class.java)
        apiService?.getUserDetail(username)
            ?.enqueue(object : Callback<UserItem?> {
                override fun onResponse(call: Call<UserItem?>, response: Response<UserItem?>) {

                    showLoading(false)

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
                        Toast.makeText(this@FavoriteUserDetailActivity, resources.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }, 2000)
                }
            })
    }

    fun showLoading(state: Boolean){
        if (state){
            binding.let {
                it.progressbar.visibility = View.VISIBLE
                it.deleteButton.visibility = View.GONE
                it.avatar.visibility = View.GONE
                it.blog.visibility = View.GONE
                it.company.visibility = View.GONE
                it.follower.visibility = View.GONE
                it.following.visibility = View.GONE
                it.location.visibility = View.GONE
                it.name.visibility = View.GONE
                it.repository.visibility = View.GONE
                it.textView10.visibility = View.GONE
                it.textView14.visibility = View.GONE
                it.textView16.visibility = View.GONE
                it.textView3.visibility = View.GONE
                it.textView6.visibility = View.GONE
                it.textView8.visibility = View.GONE
                it.username.visibility = View.GONE
            }
        }else{
            binding.let {
                it.progressbar.visibility = View.GONE
                it.deleteButton.visibility = View.VISIBLE
                it.avatar.visibility = View.VISIBLE
                it.blog.visibility = View.VISIBLE
                it.company.visibility = View.VISIBLE
                it.follower.visibility = View.VISIBLE
                it.following.visibility = View.VISIBLE
                it.location.visibility = View.VISIBLE
                it.name.visibility = View.VISIBLE
                it.repository.visibility = View.VISIBLE
                it.textView10.visibility = View.VISIBLE
                it.textView14.visibility = View.VISIBLE
                it.textView16.visibility = View.VISIBLE
                it.textView3.visibility = View.VISIBLE
                it.textView6.visibility = View.VISIBLE
                it.textView8.visibility = View.VISIBLE
                it.username.visibility = View.VISIBLE
            }
        }
    }

}