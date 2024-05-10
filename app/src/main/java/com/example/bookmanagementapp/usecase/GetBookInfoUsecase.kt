package com.example.bookmanagementapp.usecase

import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.repository.BookRepository
import javax.inject.Inject

// ユースケースのインターフェース
interface GetBookInfoUseCase {
    suspend fun execute(isbn: String): BookInfoEntity?
}

// ユースケースの実装
class GetBookInfoUseCaseImpl @Inject constructor(
    private val bookRepository: BookRepository
) : GetBookInfoUseCase {
    override suspend fun execute(isbn: String): BookInfoEntity? {
        // Google Books APIから書籍情報を取得
        val response = bookRepository.getBookInfoFromApi(isbn)
        if (response.isSuccessful) {
            // レスポンスからBookInfoEntityを作成して返す
            val bookResponse = response.body()
            return bookResponse?.items?.get(0)?.volumeInfo?.let { volumeInfo ->
                BookInfoEntity(
                    isbn = isbn,
                    title = volumeInfo.title,
                    authors = volumeInfo.authors?.joinToString(", "),
                    description = volumeInfo.description,
                    pageCount = volumeInfo.pageCount,
                    thumbnail = volumeInfo.imageLinks?.thumbnail,
                    readPageCount = 0
                )
            }
        } else {
            return null
        }
    }
}