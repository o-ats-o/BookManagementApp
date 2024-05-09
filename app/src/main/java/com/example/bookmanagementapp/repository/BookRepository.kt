package com.example.bookmanagementapp.repository

import com.example.bookmanagementapp.dao.BookDao
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.model.BookResponse
import com.example.bookmanagementapp.network.BookApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface BookRepository {
    suspend fun getBookInfo(isbn: String): BookInfoEntity?
    suspend fun saveBookInfo(bookInfo: BookInfoEntity)
    fun getAllBooks(): Flow<List<BookInfoEntity>>
    suspend fun getBookInfoFromApi(query: String): Response<BookResponse>
}

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val bookApiService: BookApiService
) : BookRepository {

    override suspend fun getBookInfo(isbn: String): BookInfoEntity? {
        return bookDao.findBookByIsbn(isbn)
    }

    override suspend fun saveBookInfo(bookInfo: BookInfoEntity) {
        bookDao.insertBook(bookInfo)
    }

    override fun getAllBooks(): Flow<List<BookInfoEntity>> {
        return bookDao.getAllBooks()
    }

    override suspend fun getBookInfoFromApi(query: String): Response<BookResponse> {
        return bookApiService.getBookInfo(query)
    }
}