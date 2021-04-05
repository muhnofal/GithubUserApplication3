package com.example.githubuserapplication3.Model

import com.google.gson.annotations.SerializedName

data class FollowerItem (

    @SerializedName("login")
    var followerUsername: String = "",

    @SerializedName("avatar_url")
    var followerAvatar: String = ""

)