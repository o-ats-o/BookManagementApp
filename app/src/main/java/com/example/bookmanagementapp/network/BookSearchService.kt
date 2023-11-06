package com.example.bookmanagementapp.network

import com.example.bookmanagementapp.model.BookInfo
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "https://www.googleapis.com"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service object for creating api calls
 */
interface BookApiService {
    @GET("/books/v1/volumes?q=isbn:{isbn}")
    suspend fun getBookInfo(
        @Path("isbn") isbn: String
    ): List<BookInfo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BookApi {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}