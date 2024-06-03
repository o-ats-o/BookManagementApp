package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressInfoViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    // readPageCountとpageCountの値を保持するStateFlowを追加
    private val _readPageCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _pageCount: MutableStateFlow<Int> = MutableStateFlow(0)

    // 現在のページ数が総ページを超えていないかを判定するStateFlowを追加
    private val _isNotOverPageCount = MutableStateFlow(false)
    val isNotOverPageCount: StateFlow<Boolean> = _isNotOverPageCount

    init {

        viewModelScope.launch {
            combine(_readPageCount, _pageCount) { readPageCount, pageCount ->
               readPageCount <= pageCount
            }.collect {
                _isNotOverPageCount.value = it
            }
        }
    }

    // readPageCountとpageCountの値を更新するメソッドを追加
    fun updateReadPageCount(newReadPageCount: Int) {
        _readPageCount.value = newReadPageCount
    }

    fun updatePageCount(newPageCount: Int) {
        _pageCount.value = newPageCount
    }

    fun updateBookInfo(book: BookInfoEntity) {
        _readPageCount.value = book.readPageCount
        _pageCount.value = book.pageCount
    }

    suspend fun updateBookProgress(isbn: String) {
        val readPageCount = _readPageCount.value
        val pageCount = _pageCount.value
        val bookInfo = bookRepository.getBookInfo(isbn)
        if (bookInfo != null) {
            val updatedBookInfo = bookInfo.copy(
                authors = bookInfo.authors,
                readPageCount = readPageCount,
                pageCount = pageCount
            )
            bookRepository.saveBookInfo(updatedBookInfo)
        }
    }
}