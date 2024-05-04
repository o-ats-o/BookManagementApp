package com.example.bookmanagementapp.dao

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

    @Query("SELECT * FROM book_info")
    fun getAllBooks(): Flow<List<BookInfoEntity>>

    @Query("SELECT * FROM book_info WHERE isbn = :isbn LIMIT 1")
    suspend fun findBookByIsbn(isbn: String): BookInfoEntity?
}