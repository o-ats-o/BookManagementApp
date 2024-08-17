package com.example.bookmanagementapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookmanagementapp.model.BookInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookInfoEntity)

    @Query("UPDATE book_info SET readPageCount = :readPageCount, pageCount = :pageCount WHERE isbn = :isbn")
    suspend fun updateBookProgress(isbn: String, readPageCount: Int, pageCount: Int)

    @Query("SELECT * FROM book_info")
    fun getAllBooks(): Flow<List<BookInfoEntity>>

    @Query("SELECT * FROM book_info WHERE isbn = :isbn LIMIT 1")
    suspend fun findBookByIsbn(isbn: String): BookInfoEntity?
}