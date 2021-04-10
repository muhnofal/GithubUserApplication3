package com.example.githubuserapplication3

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuserapplication3.adapter.ViewPagerAdapter
import com.example.githubuserapplication3.data.ApiService
import com.example.githubuserapplication3.data.DataRetrofit.getData
import com.example.githubuserapplication3.databinding.ActivityUserDetailBinding
import com.example.githubuserapplication3.db.DatabaseContract
import com.example.githubuserapplication3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuserapplication3.db.FavoriteHelper
import com.example.githubuserapplication3.model.UserItem
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var favoriteHelper: FavoriteHelper
    val values = ContentValues()
    private var statusFavorite: Boolean = false
    private var user: UserItem? = null

    companion object {
        const val EXTRA_USER = "extra_username"
        private const val TAG_CHECK_FAVORITE_2 = "check_favorite_2"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Action bar title
        supportActionBar?.title = getString(R.string.user_detail)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        //get data from MainActivity
        user = intent.getParcelableExtra(EXTRA_USER)

        //get username from activity
        val username: String? = user?.username

        //ViewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        viewPagerAdapter.id = username //Send Data to ViewPagerAdapter

        //visibility
        showLoading(true)

        //Get data from API
        requestData(username.orEmpty())

        favoriteCheck()
        binding.floatingActionButton.setOnClickListener {
            if (statusFavorite) {
                favoriteHelper.deleteByUsername(user?.username.toString())
                statusFavorite = false
                setStatusFavorite(statusFavorite)
            } else {
                contentResolver.insert(CONTENT_URI, values)
                statusFavorite = true
                setStatusFavorite(statusFavorite)
                Log.d(TAG_CHECK_FAVORITE_2, "favoriteCheck2:  " + statusFavorite)
            }
        }

    }

    //from activity
    private fun requestData(id: String) {
        val apiService = getData()?.create(ApiService::class.java)
        apiService?.getUserDetail(id)?.enqueue(object : Callback<UserItem?> {
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

                Glide.with(this@UserDetailActivity).load(avatar).into(binding.avatar)
                binding.let {
                    it.name.text = name
                    it.username.text = username
                    it.company.text = company
                    it.blog.text = blog
                    it.location.text = location
                    it.repository.text = repository.toString()
                    it.follower.text = follower.toString()
                    it.following.text = following.toString()
                }

                //put database
                values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, avatar)

            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    Toast.makeText(this@UserDetailActivity, resources.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }, 2000)
            }

        })
    }

    fun showLoading(state: Boolean) {
        if (state) {
            binding.progressbar.visibility = View.VISIBLE
            binding.topContainer.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
        } else {
            binding.progressbar.visibility = View.GONE
            binding.topContainer.visibility = View.VISIBLE
            binding.topContainer.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    @JvmName("setStatusFavorite1")
    fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun favoriteCheck() {
        val value: String? = user?.username
        val cursor: Cursor = favoriteHelper.queryByUsername(value)
        if (cursor.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

}