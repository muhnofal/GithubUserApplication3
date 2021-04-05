package com.example.githubuserapplication3.Model
import com.google.gson.annotations.SerializedName

data class UserItem(

    @SerializedName("login")
    var username: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("company")
    var company: String = "",
    @SerializedName("blog")
    var blog: String = "",
    @SerializedName("location")
    var location: String = "",
    @SerializedName("avatar_url")
    var image: String = "",
    @SerializedName("public_repos")
    var repos: Int = 0,
    @SerializedName("followers")
    var followers: Int = 0,
    @SerializedName("following")
    var following: Int = 0

)