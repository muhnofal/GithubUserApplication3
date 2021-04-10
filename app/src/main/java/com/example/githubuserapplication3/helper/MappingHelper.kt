package com.example.githubuserapplication3.helper

import android.database.Cursor
import com.example.githubuserapplication3.db.DatabaseContract
import com.example.githubuserapplication3.model.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                favoriteList.add(Favorite(id, avatar, username))
            }
        }
        return favoriteList
    }

    fun mapCursorTopObject(favoriteCursor: Cursor?): Favorite {
        var favorite = Favorite()
        favoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
            favorite = Favorite(id, username, avatar)
        }
        return favorite
    }

}