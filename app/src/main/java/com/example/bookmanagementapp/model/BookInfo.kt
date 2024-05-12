package com.example.bookmanagementapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BookResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<BookItem>
)

data class BookItem(
    val id: String,
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val pageCount: Int?,
    val imageLinks: ImageLinks?,
)

data class ImageLinks(
    val thumbnail: String?
)

@Entity(tableName = "book_info")
data class BookInfoEntity(
    @PrimaryKey
    val isbn: String,
    val title: String?,
    val authors: String?,
    val description: String?,
    val pageCount: Int?,
    val thumbnail: String?,
    val readPageCount: Int?
)