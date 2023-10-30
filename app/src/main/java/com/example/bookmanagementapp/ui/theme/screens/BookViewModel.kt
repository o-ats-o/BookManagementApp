package com.example.bookmanagementapp.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagementapp.network.BooksApi
import kotlinx.coroutines.launch

sealed interface BookUiState {
    data class Success(val information: String) : BookUiState
    object Error : BookUiState
    object Loading : BookUiState
}

class BookViewModel : ViewModel() {
    private fun getBookInformation() {
        viewModelScope.launch {
            val listResult = BooksApi.retrofitService.getBook()
            bookUiState = listResult
        }
    }
}
