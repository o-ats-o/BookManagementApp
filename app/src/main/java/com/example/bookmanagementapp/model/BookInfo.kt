package com.example.bookmanagementapp.model

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