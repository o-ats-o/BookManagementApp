package com.example.bookmanagementapp.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun BookInfoScreen(
    viewMode: BookViewModel,
    isbn: String
){
    val bookInfoState = viewMode.bookInfoState.collectAsState()

    LaunchedEffect(isbn) {
        Log.e("BookInfoScreen", "Received ISBN: $isbn") // 追加
        viewMode.getBookInfo(isbn)
    }

    val bookInfo = bookInfoState.value
    if (bookInfo != null) {
        Column {
            Text(
                text = "Title: ${bookInfo.title}"
            )
            Text(
                text = "Authors: ${bookInfo.authors.joinToString(", ")}"
            )
            Text(
                text = "Description: ${bookInfo.description}"
            )
        }
    }

}