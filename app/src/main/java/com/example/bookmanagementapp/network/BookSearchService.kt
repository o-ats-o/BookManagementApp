package com.example.bookmanagementapp.network

import com.example.bookmanagementapp.model.BookResponse
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://www.googleapis.com"

//　Moshiのインスタンスを作成
private val moshi =
    Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

// Retrofitのインスタンスを作成
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

val service: BookApiService = retrofit.create(BookApiService::class.java)

interface BookApiService {
    @GET("books/v1/volumes")
    suspend fun getBookInfo(
        @Query("q") query: String
    ): Response<BookResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
