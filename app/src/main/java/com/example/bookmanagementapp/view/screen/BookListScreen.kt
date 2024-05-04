package com.example.bookmanagementapp.view.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookmanagementapp.viewmodel.BookInfoViewModel

@Composable
fun BookListScreen(
    viewModel: BookInfoViewModel = hiltViewModel()
) {
    val allBooks = viewModel.allBooks.collectAsState()

    LazyColumn {
        items(allBooks.value.size) { index ->
            val book = allBooks.value[index]
            book.title?.let { Text(text = it) }
            // 他の書籍情報も表示する
        }
    }
}