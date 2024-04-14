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
    val publishedDate: String?,
    val description: String?,
    val industryIdentifiers: List<IndustryIdentifier>?,
    val pageCount: Int?,
    val printType: String?,
    val maturityRating: String?,
    val allowAnonLogging: Boolean?,
    val contentVersion: String?,
    val imageLinks: ImageLinks?,
    val language: String?,
    val previewLink: String?,
    val infoLink: String?,
    val canonicalVolumeLink: String?
)

data class IndustryIdentifier(
    val type: String?,
    val identifier: String?
)

data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
)