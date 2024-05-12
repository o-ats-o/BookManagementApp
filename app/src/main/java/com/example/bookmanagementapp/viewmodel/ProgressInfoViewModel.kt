package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.repository.BookRepository
import com.example.bookmanagementapp.usecase.GetBookInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressInfoViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
    private val bookRepository: BookRepository
) : ViewModel() {

    // readPageCountとpageCountの値を保持するStateFlowを追加
    private val _readPageCount: MutableStateFlow<Int?>
    private val _pageCount: MutableStateFlow<Int?>

    // 進捗情報が空でないかどうかを保持するStateFlowを追加
    private val _isNotEmpty = MutableStateFlow(false)
    val isNotEmpty: StateFlow<Boolean> = _isNotEmpty

    init {
        _readPageCount = MutableStateFlow(null)
        _pageCount = MutableStateFlow(null)

        viewModelScope.launch {
            combine(_readPageCount, _pageCount) { readPageCount, pageCount ->
                readPageCount != null && pageCount != null && (readPageCount <= pageCount)
            }.collect {
                _isNotEmpty.value = it
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
        if (readPageCount != null && pageCount != null) {
            val bookInfo = getBookInfoUseCase.execute(isbn)
            if (bookInfo != null) {
                val updatedBookInfo = bookInfo.copy(readPageCount = readPageCount, pageCount = pageCount)
                bookRepository.saveBookInfo(updatedBookInfo)
            }
        }
    }
}