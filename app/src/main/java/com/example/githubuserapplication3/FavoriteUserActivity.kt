package com.example.githubuserapplication3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication3.Adapter.FavoriteListAdapter
import com.example.githubuserapplication3.databinding.ActivityFavoriteUserBinding
import com.example.githubuserapplication3.db.FavoriteHelper
import com.example.githubuserapplication3.helper.MappingHelper

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

        //get data
        getDatabase()

    }

    private fun getDatabase(){
        val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        val cursor = favoriteHelper.queryAll()
        val favorite = MappingHelper.mapCursorToArrayList(cursor)
        favoriteHelper.close()
        if(favorite.size > 0){
            adapter.listFavorites = favorite
        }else{
            adapter.listFavorites = ArrayList()
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            when(requestCode){
                FavoriteUserDetailActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(FavoriteUserDetailActivity.EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                    Toast.makeText(this, "Data Telah terhapus", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}