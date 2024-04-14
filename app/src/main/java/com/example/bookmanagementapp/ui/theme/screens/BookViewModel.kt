package com.example.bookmanagementapp.ui.theme.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.model.BookResponse
import com.example.bookmanagementapp.network.BookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class BookViewModel(private val bookApiService: BookApiService) : ViewModel() {

    private val _bookInfoState: MutableStateFlow<BookInfo?> = MutableStateFlow(null)
    val bookInfoState: StateFlow<BookInfo?> get() = _bookInfoState

    private var currentJob: Job? = null

    fun getBookInfo(isbn: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            val response: Response<BookResponse> = bookApiService.getBookInfo("isbn:$isbn")
            if (response.isSuccessful) {
                Log.d("BookViewModel", "response: ${response.body()}")
                val bookResponse: BookResponse? = response.body()
                val bookInfo: BookInfo? = bookResponse?.items?.firstOrNull()?.volumeInfo
                _bookInfoState.value = bookInfo
            }else{
                val errorBody = response.errorBody()?.string()
                Log.e("BookViewModel", "response: $errorBody")
            }
        }
    }
}