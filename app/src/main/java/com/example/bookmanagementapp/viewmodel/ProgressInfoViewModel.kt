package com.example.bookmanagementapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookmanagementapp.repository.BookRepository
import com.example.bookmanagementapp.usecase.GetBookInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProgressInfoViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
    private val bookRepository: BookRepository
) : ViewModel() {

    suspend fun updateBookProgress(isbn: String, readPageCount: Int, pageCount: Int) {
        val bookInfo = getBookInfoUseCase.execute(isbn)
        if (bookInfo != null) {
            val updatedBookInfo = bookInfo.copy(readPageCount = readPageCount, pageCount = pageCount)
            bookRepository.saveBookInfo(updatedBookInfo)
        }
    }
}