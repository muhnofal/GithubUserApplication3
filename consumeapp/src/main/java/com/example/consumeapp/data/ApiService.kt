package com.example.consumeapp.data

import com.example.consumeapp.model.FollowerItem
import com.example.consumeapp.model.FollowingItem
import com.example.consumeapp.model.User
import com.example.consumeapp.model.UserItem
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