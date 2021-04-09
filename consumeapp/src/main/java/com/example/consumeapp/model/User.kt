package com.example.consumeapp.model

import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("items")
    private lateinit var users: List<UserItem>

    fun getUsers(): List<UserItem>{
        return users
    }

}