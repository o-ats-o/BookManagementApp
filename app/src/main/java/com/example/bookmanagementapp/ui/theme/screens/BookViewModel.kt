package com.example.bookmanagementapp.ui.theme.screens

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.network.BookApi
import com.example.bookmanagementapp.ui.theme.isbn
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BookUiState {
    data class Success(val info: String) : BookUiState
    object Error : BookUiState
    object Loading : BookUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class BookViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var bookUiState: BookUiState by mutableStateOf(BookUiState.Loading)
        private set

    /**
     * Call getBookPhotos() on init so we can display status immediately.
     */
    init {
        getBookInfo()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBookInfo() {
        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                val listResult = BookApi.retrofitService.getBookInfo(isbn)
                BookUiState.Success(
                    "Success: ${listResult.size} Book retrieved"
                )
            } catch (e: IOException) {
                BookUiState.Error
            } catch (e: HttpException) {
                BookUiState.Error
            }
        }
    }
}
