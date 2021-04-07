package com.example.githubuserapplication3

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication3.Adapter.FavoriteListAdapter
import com.example.githubuserapplication3.databinding.ActivityFavoriteUserBinding
import com.example.githubuserapplication3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuserapplication3.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteListAdapter
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.favoriteRecycler.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecycler.setHasFixedSize(true)
        adapter = FavoriteListAdapter(this)
        binding.favoriteRecycler.adapter = adapter

        //provider?
        val handlerThread = HandlerThread("DatabaseObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                getDatabase()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    private fun getDatabase(){

        GlobalScope.launch(Dispatchers.Main){
            val defferedFavorites = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = defferedFavorites.await()
            if(favorite.size > 0){
                adapter.listFavorites = favorite
            }else{
                adapter.listFavorites = ArrayList()
                adapter.notifyDataSetChanged()
                Toast.makeText(this@FavoriteUserActivity, "No Data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getDatabase()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}