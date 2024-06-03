package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel() {

    private val allBooksFlow: Flow<List<BookInfoEntity>> = bookRepository.getAllBooks()

    private val _allBooks = MutableStateFlow<List<BookInfoEntity>>(emptyList())
    val allBooks: StateFlow<List<BookInfoEntity>> = _allBooks

    private val _readingProgress = MutableStateFlow<List<Pair<BookInfoEntity, Float>>>(emptyList())
    val readingProgress: StateFlow<List<Pair<BookInfoEntity, Float>>> = _readingProgress

    init {
        // DBから書籍情報の取得を行うのでIOスレッドで実行
        viewModelScope.launch(Dispatchers.IO) {
            allBooksFlow.collect { books ->
                val progressList = books.map { book ->
                    val progress = if (book.pageCount != 0) {
                        calculateReadingProgress(book.readPageCount, book.pageCount)
                    } else {
                        0f
                    }
                    Pair(book, progress)
                }
                // stateFlowの値を更新するのでMainスレッドで実行
                withContext(Dispatchers.Main) {
                    _allBooks.value = books
                    _readingProgress.value = progressList
                }
            }
        }
    }

    private fun calculateReadingProgress(readPageCount: Int, pageCount: Int): Float {
        return if (pageCount!= 0) {
            readPageCount.toFloat() / pageCount.toFloat()
        } else {
            0f
        }
    }
}