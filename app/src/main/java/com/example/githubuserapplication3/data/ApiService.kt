package com.example.githubuserapplication3.data

import com.example.githubuserapplication3.model.FollowerItem
import com.example.githubuserapplication3.model.FollowingItem
import com.example.githubuserapplication3.model.User
import com.example.githubuserapplication3.model.UserItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/search/users?q=")
    @Headers("Authorization: token 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")
    open fun getUser(@Query("q") username: String): Call<User>

    @GET("/users/{username}")
    open fun getUserDetail(@Path("username") username: String): Call<UserItem>

    @GET("/users/{username}/followers")
    open fun getFollower(@Path("username") username: String): Call<List<FollowerItem>>

    @GET("/users/{username}/following")
    open fun getFollowing(@Path("username") username: String): Call<List<FollowingItem>>
    //TEST BRANCH

}