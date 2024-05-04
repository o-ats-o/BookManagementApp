package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.dao.BookDao
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.network.BookApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BookInfoViewState<out T> {
    data object Loading : BookInfoViewState<Nothing>()
    data class Success<T>(val data: T) : BookInfoViewState<T>()
    data class Error(val message: String) : BookInfoViewState<Nothing>()
}

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val bookApiService: BookApiService,
    private val bookDao: BookDao,
) : ViewModel() {
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

    // エラーメッセージを保持するStateFlowを追加
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun saveBookInfoToLocalDatabase(isbn: String, bookInfo: BookInfo) {
        viewModelScope.launch {
            val existingBook = bookDao.findBookByIsbn(isbn)
            if (existingBook == null) {
                val bookInfoEntity = BookInfoEntity(
                    isbn = isbn,
                    title = bookInfo.title,
                    authors = bookInfo.authors?.joinToString(", "),
                    description = bookInfo.description,
                    pageCount = bookInfo.pageCount,
                    thumbnail = bookInfo.imageLinks?.thumbnail
                )
                bookDao.insertBook(bookInfoEntity)
            } else {
                // 書籍が既に存在するため、保存をスキップ
                // 必要に応じてユーザーに通知
                _errorMessage.value = "この書籍はすでに登録されています"
            }
        }
    }

    // 書籍情報を取得
    private val _allBooks = MutableStateFlow<List<BookInfoEntity>>(emptyList())
    val allBooks: StateFlow<List<BookInfoEntity>> = _allBooks

    init {
        viewModelScope.launch {
            bookDao.getAllBooks().collect {
                _allBooks.value = it
            }
        }
    }
}