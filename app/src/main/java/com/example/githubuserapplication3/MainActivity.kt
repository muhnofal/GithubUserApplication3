package com.example.githubuserapplication3

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication3.Adapter.UserListAdapter
import com.example.githubuserapplication3.Data.ApiService
import com.example.githubuserapplication3.Model.User
import com.example.githubuserapplication3.Model.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.widget.SearchView
import com.example.githubuserapplication3.Data.DataRetrofit
import com.example.githubuserapplication3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //RecyclerView
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)

        //Action bar title
        supportActionBar?.title = getString(R.string.app_name)
        showClickSearch(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(searchText: String?): Boolean {

                if (searchText?.isNotEmpty() == true) {
                    val search: String = searchText.toLowerCase(Locale.ROOT)
                    showLoading(true)
                    showClickSearch(false)
                    showNoData(false)
                    setData(search)
                }

                return true
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //setting localization
        if (item.itemId == R.id.action_change_language_setting){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

        if (item.itemId == R.id.favorite){
            val mIntent = Intent(this, FavoriteUserActivity::class.java)
            startActivity(mIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    fun setData(username: String){
        val listItem = ArrayList<UserItem>()
        val apiService = DataRetrofit.getData()?.create(ApiService::class.java)
        apiService?.getUser(username)
                ?.enqueue(object : Callback<User> {

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val items: List<UserItem>? = response.body()?.getUsers()

                        showLoading(false)
                        showNoData(false)

                        for (userItem in items.orEmpty()) {
                            listItem.add(userItem)
                        }

                        val adapter = UserListAdapter(this@MainActivity)
                        adapter.list = listItem

                        binding.recycler.adapter = adapter
                        binding.recycler.adapter?.notifyDataSetChanged()

                        if (listItem.isEmpty()) {
                            showNoData(true)
                        }

                    }

                    override fun onFailure(call: Call<User?>, t: Throwable) {
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            Toast.makeText(this@MainActivity, resources.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }, 2000)
                    }
                })
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.activityMainProgressBar.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        }else{
            binding.activityMainProgressBar.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
        }
    }

    private fun showNoData(state: Boolean){
        if(state){
            binding.noData.visibility = View.VISIBLE
        }else{
            binding.noData.visibility = View.GONE
        }
    }

    private fun showClickSearch(state: Boolean){
        if(state){
            binding.clickSearchIcon.visibility = View.VISIBLE
        }else{
            binding.clickSearchIcon.visibility = View.GONE
        }
    }

}