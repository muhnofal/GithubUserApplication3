package com.example.consumeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(

    var id: Int = 0,
    var avatar: String = "",
    var username: String = ""

): Parcelable