package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.repository.BookRepository
import com.example.bookmanagementapp.usecase.GetBookInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val getBookInfoUseCase: GetBookInfoUseCase,
) : ViewModel() {
    private var job: Job? = null

    private val _bookInfoState = MutableStateFlow<BookInfoViewState>(BookInfoViewState.Loading)
    val bookInfoState: StateFlow<BookInfoViewState> = _bookInfoState

    fun getBookInfo(isbn: String) {
        job?.cancel()
        // APIから書籍情報を取得するのでIOスレッドで実行
        job = viewModelScope.launch(Dispatchers.IO) {
            _bookInfoState.value = BookInfoViewState.Loading
            try {
                val bookInfoEntity = getBookInfoUseCase.execute(isbn)
                if (bookInfoEntity != null) {
                    _bookInfoState.value = BookInfoViewState.Success(bookInfoEntity)
                } else {
                    throw Exception("Book info not found")
                }
            } catch (exception: Exception) {
                _bookInfoState.value = BookInfoViewState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    // エラーメッセージを保持するStateFlowを追加
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun saveBookInfoToLocalDatabase(
        isbn: String,
        bookInfo: BookInfo,
        userEnteredTitle: String,
        userEnteredAuthors: String,
        userEnteredDescription: String,
        userEnteredPageCount: String
    ) {
        viewModelScope.launch {
            val existingBook = bookRepository.getBookInfo(isbn)
            if (existingBook == null) {
                val bookInfoEntity = BookInfoEntity(
                    isbn = isbn,
                    title = userEnteredTitle,
                    authors = userEnteredAuthors,
                    description = userEnteredDescription,
                    pageCount = userEnteredPageCount.toInt(),
                    thumbnail = bookInfo.imageLinks?.thumbnail,
                    readPageCount = 0
                )
                bookRepository.saveBookInfo(bookInfoEntity)
            } else {
                // 書籍が既に存在するため、保存をスキップ
                // エラーメッセージを設定
                _errorMessage.value = "この書籍はすでに登録されています"
            }
        }
    }
}

sealed class BookInfoViewState {
    data object Loading : BookInfoViewState()
    data class Success(val data: BookInfoEntity) : BookInfoViewState()
    data class Error(val message: String) : BookInfoViewState()
}