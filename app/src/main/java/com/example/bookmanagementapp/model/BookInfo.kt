package com.example.bookmanagementapp.model

import kotlinx.serialization.Serializable


@Serializable
data class BookInfo(
    val title: String,
    val authors: List<String>
)