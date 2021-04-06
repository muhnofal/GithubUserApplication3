package com.example.githubuserapplication3.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {


    internal class FavoriteColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar"
            const val AUTHORITY = "com.example.githubuserapplication3"
            const val SCHEME = "content"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }

}