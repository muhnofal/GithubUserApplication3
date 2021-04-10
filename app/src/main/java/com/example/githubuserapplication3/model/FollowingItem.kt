package com.example.githubuserapplication3.model

import com.google.gson.annotations.SerializedName

data class FollowingItem(

        @SerializedName("login")
        var followingUsername: String = "",

        @SerializedName("avatar_url")
        var followingAvatar: String = ""

)