package com.example.bookmanagementapp.model

import com.squareup.moshi.Json

data class BookInfo(
    @Json(name = "title")
    val title: String,
    @Json(name = "authors")
    val authors: List<String>
)