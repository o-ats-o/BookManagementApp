package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.dao.BookDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    bookDao: BookDao
) : ViewModel() {

    // 書籍情報を取得
    private val allBooksFlow: Flow<List<BookInfoEntity>> = bookDao.getAllBooks()

    // 書籍情報のStateFlow
    private val _allBooks = MutableStateFlow<List<BookInfoEntity>>(emptyList())
    val allBooks: StateFlow<List<BookInfoEntity>> = _allBooks

    // 読書進行状況のStateFlow
    private val _readingProgress = MutableStateFlow<List<Pair<BookInfoEntity, Float>>>(emptyList())
    val readingProgress: StateFlow<List<Pair<BookInfoEntity, Float>>> = _readingProgress

    init {
        viewModelScope.launch {
            allBooksFlow.collect { books ->
                _allBooks.value = books
                _readingProgress.value = books.map { book ->
                    val progress = calculateReadingProgress(book.readPageCount, book.pageCount)
                    Pair(book, progress)
                }
            }
        }
    }

    private fun calculateReadingProgress(readPageCount: Int?, pageCount: Int?): Float {
        return if (pageCount!= null && readPageCount!= null && pageCount!= 0) {
            readPageCount.toFloat() / pageCount.toFloat()
        } else {
            0f
        }
    }
}