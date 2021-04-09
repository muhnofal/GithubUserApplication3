package com.example.consumeapp.model

import com.google.gson.annotations.SerializedName

data class FollowerItem (

    @SerializedName("login")
    var followerUsername: String = "",

    @SerializedName("avatar_url")
    var followerAvatar: String = ""

)