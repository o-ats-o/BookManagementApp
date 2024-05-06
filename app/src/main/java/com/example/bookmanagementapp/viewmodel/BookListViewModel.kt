package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.dao.BookDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    bookDao: BookDao
) : ViewModel() {
    private val allBooksFlow: Flow<List<BookInfoEntity>> = bookDao.getAllBooks()

    val readingProgressFlow: Flow<List<Pair<BookInfoEntity, Float>>> = allBooksFlow.map { books ->
        books.map { book ->
            val progress = calculateReadingProgress(book.readPageCount, book.pageCount)
            Pair(book, progress)
        }
    }

    val readingProgress = readingProgressFlow.asLiveData()

    private fun calculateReadingProgress(readPageCount: Int?, pageCount: Int?): Float {
        return if (pageCount != null && readPageCount != null && pageCount != 0) {
            readPageCount.toFloat() / pageCount.toFloat()
        } else {
            0f
        }
    }
}