package com.example.bookmanagementapp.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookmanagementapp.model.BookInfoEntity

@Composable
fun ProgressInfoScreen(book: BookInfoEntity) {
    var readPageCount by remember { mutableStateOf(book.readPageCount.toString()) }
    var pageCount by remember { mutableStateOf(book.pageCount.toString()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Title: ${book.title}")
        TextField(
            value = readPageCount,
            onValueChange = { readPageCount = it },
            label = { Text("Read Pages") }
        )
        Spacer(modifier = Modifier.padding(top = 28.dp))
        TextField(
            value = pageCount,
            onValueChange = { pageCount = it },
            label = { Text("Total Pages") }
        )
    }
}

@Preview
@Composable
fun PreviewProgressInfoScreen() {
    ProgressInfoScreen(
        book = BookInfoEntity(
            isbn = "978-4-04-713928-8",
            title = "Kotlinスタートブック",
            authors = "渡辺 竜王",
            description = "Kotlinの基本から応用までを網羅した入門書",
            pageCount = 360,
            thumbnail = "https://watanabejunma.github.io/kotlin-book-cover.jpg",
            readPageCount = 100
        )
    )
}