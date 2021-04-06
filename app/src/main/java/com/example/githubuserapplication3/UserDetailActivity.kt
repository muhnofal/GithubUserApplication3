package com.example.githubuserapplication3
import android.content.ContentValues
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuserapplication3.Adapter.ViewPagerAdapter
import com.example.githubuserapplication3.Data.ApiService
import com.example.githubuserapplication3.Data.DataRetrofit.getData
import com.example.githubuserapplication3.Model.UserItem
import com.example.githubuserapplication3.databinding.ActivityUserDetailBinding
import com.example.githubuserapplication3.db.DatabaseContract
import com.example.githubuserapplication3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuserapplication3.db.FavoriteHelper
import com.example.githubuserapplication3.helper.MappingHelper
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    private lateinit var favoriteHelper: FavoriteHelper
    val values = ContentValues()
    private var statusFavorite: Boolean = false

    companion object{
        const val EXTRA_USERNAME = "extra_username"
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

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        //Get ID
        val username: String? = intent.getStringExtra(EXTRA_USERNAME)

        //ViewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        //Send Data to ViewPagerAdapter
        viewPagerAdapter.id = username

        //Action bar title
        supportActionBar?.title = getString(R.string.app_name)

        //visibility
        showLogin(true)

        //Get data from API
        requestData(username.orEmpty())

        //insert to database
        setStatusFavorite(statusFavorite)
        binding.floatingActionButton.setOnClickListener{
            statusFavorite = !statusFavorite

            if(statusFavorite){

                val handlerThread = HandlerThread("DataObserver")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)

                val myObserver = object : ContentObserver(handler){
                    override fun onChange(selfChange: Boolean) {
                        loadFavoriteAsync()
                    }
                }


            }else{
                favoriteHelper.deleteById("id")
                Toast.makeText(this, "Menghapus", Toast.LENGTH_SHORT).show()
            }
            setStatusFavorite(statusFavorite)
        }

    }

    private fun requestData(id: String) {
        val apiService = getData()?.create(ApiService::class.java)
        apiService?.getUserDetail(id)?.enqueue(object : Callback<UserItem?> {
            override fun onResponse(call: Call<UserItem?>, response: Response<UserItem?>) {

                showLogin(false)

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
                binding.name.text = name
                binding.username.text = username
                binding.company.text = company
                binding.blog.text = blog
                binding.location.text = location
                binding.repository.text = repository.toString()
                binding.follower.text = follower.toString()
                binding.following.text = following.toString()

                //put database
                values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, avatar)

            }

            override fun onFailure(call: Call<UserItem?>, t: Throwable) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    Toast.makeText(this@UserDetailActivity, "Check Internet", Toast.LENGTH_SHORT).show()
                    showLogin(false)
                }, 2000)
            }

        })
    }

    fun showLogin(state: Boolean){
        if(state){
            binding.progressbar.visibility = View.VISIBLE
            binding.topContainer.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
        }else{
            binding.progressbar.visibility = View.GONE
            binding.topContainer.visibility = View.VISIBLE
            binding.topContainer.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    private fun loadFavoriteAsync(){

        GlobalScope.launch(Dispatchers.Main) {
            val defferedFavorite = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
        }

        val result = favoriteHelper.insert(values)
        if(result <= 0){
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Add to favorite", Toast.LENGTH_SHORT).show()
            favoriteHelper.close()
        }
    }

    @JvmName("setStatusFavorite1")
    fun setStatusFavorite(statusFavorite: Boolean){
        if(statusFavorite){
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

}