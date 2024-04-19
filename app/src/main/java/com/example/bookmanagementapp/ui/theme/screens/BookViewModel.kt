package com.example.bookmanagementapp.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.network.BookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class BookInfoViewState<out T> {
    object Loading : BookInfoViewState<Nothing>()
    data class Success<T>(val data: T) : BookInfoViewState<T>()
    data class Error(val message: String) : BookInfoViewState<Nothing>()
}

class BookViewModel(private val bookApiService: BookApiService) : ViewModel() {
    private var job: Job? = null

    private val _bookInfoState = MutableStateFlow<BookInfoViewState<BookInfo>>(BookInfoViewState.Loading)
    val bookInfoState: StateFlow<BookInfoViewState<BookInfo>> = _bookInfoState

    fun getBookInfo(isbn: String) {
        job?.cancel()
        // APIから書籍情報を取得するのでIOスレッドで実行
        job = viewModelScope.launch(Dispatchers.IO) {
            _bookInfoState.value = BookInfoViewState.Loading
            try {
                val response = bookApiService.getBookInfo("isbn:$isbn")
                if (response.isSuccessful) {
                    val bookResponse = response.body()
                    val bookInfo = bookResponse?.items?.firstOrNull()?.volumeInfo
                    if (bookInfo != null) {
                        _bookInfoState.value = BookInfoViewState.Success(bookInfo)
                    } else {
                        throw Exception("Book info not found")
                    }
                } else {
                    throw Exception("Error: ${response.message()}")
                }
            } catch (exception: Exception) {
                _bookInfoState.value = BookInfoViewState.Error(exception.message ?: "Unknown error")
            }
        }
    }
}