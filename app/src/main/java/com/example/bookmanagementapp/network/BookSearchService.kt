package com.example.bookmanagementapp.network

import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.ui.theme.isbn
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

var BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:${isbn}"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()
/**
 * Retrofit service object for creating api calls
 */
interface BookApiService {
    @GET("")
    suspend fun getInfo(): List<BookInfo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BookApi {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}