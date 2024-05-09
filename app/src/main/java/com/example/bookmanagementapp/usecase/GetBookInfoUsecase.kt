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
        return bookRepository.getBookInfo(isbn)
    }
}