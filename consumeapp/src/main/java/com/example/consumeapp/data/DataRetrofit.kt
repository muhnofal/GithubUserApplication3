package com.example.consumeapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataRetrofit {

    private const val Base_url = "https://api.github.com/"
    private var retrofit: Retrofit? = null

    fun getData(): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl(Base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

}