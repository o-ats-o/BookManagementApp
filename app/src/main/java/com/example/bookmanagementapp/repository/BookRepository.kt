package com.example.bookmanagementapp.repository

import com.example.bookmanagementapp.room.BookDao
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.model.BookResponse
import com.example.bookmanagementapp.network.BookApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface BookRepository {
    suspend fun getBookInfo(isbn: String): BookInfoEntity?
    suspend fun saveBookInfo(bookInfo: BookInfoEntity)
    suspend fun updateBookProgress(isbn: String, readPageCount: Int, pageCount: Int)
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

    override suspend fun updateBookProgress(isbn: String, readPageCount: Int, pageCount: Int) {
        bookDao.updateBookProgress(isbn, readPageCount, pageCount)
    }

    override fun getAllBooks(): Flow<List<BookInfoEntity>> {
        return bookDao.getAllBooks()
    }

    override suspend fun getBookInfoFromApi(query: String): Response<BookResponse> {
        return bookApiService.getBookInfo(query)
    }
}