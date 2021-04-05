package com.example.githubuserapplication3.helper

import android.database.Cursor
import com.example.githubuserapplication3.Model.Favorite
import com.example.githubuserapplication3.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite>{
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                favoriteList.add(Favorite(id, avatar, username))
            }
        }
        return favoriteList
    }

}