package com.example.bookmanagementapp.network

import com.example.bookmanagementapp.IsbnScanner
import com.example.bookmanagementapp.isbn
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private var BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=$isbn"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BooksSearchApi {
    suspend fun getBook(): String
}

object BooksApi {
    val retrofitService: BooksSearchApi by lazy {
        retrofit.create(BooksSearchApi::class.java)
    }
}