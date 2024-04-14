package com.example.bookmanagementapp.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation

@Composable
fun BookInfoScreen(
    viewMode: BookViewModel,
    isbn: String,
    modifier: Modifier = Modifier
){
    val bookInfoState = viewMode.bookInfoState.collectAsState()

    LaunchedEffect(isbn) {
        viewMode.getBookInfo(isbn)
    }

    val bookInfo = bookInfoState.value
    if (bookInfo != null) {
        val title = remember { mutableStateOf(bookInfo.title ?: "") }
        val authors = remember { mutableStateOf(bookInfo.authors?.joinToString(", ") ?: "") }
        val description = remember { mutableStateOf(bookInfo.description ?: "") }
        val pageCount = remember { mutableStateOf(bookInfo.pageCount?.toString() ?: "") }

        Column (
            modifier = modifier
                .padding(18.dp)
                .padding(top = 24.dp)
        ) {
            bookInfo.imageLinks?.let { imageLinks ->
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = imageLinks.thumbnail).apply(block = fun ImageRequest.Builder.() {
                                transformations(RoundedCornersTransformation(10f))
                            }).build(),
                    ),
                    contentDescription = "Book cover image",
                    modifier = Modifier
                        .height(240.dp)
                        .width(160.dp)
                        .align(CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = title.value,
                onValueChange = { newValue -> title.value = newValue },
                label = { Text("タイトル") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = authors.value,
                onValueChange = { newValue -> authors.value = newValue },
                label = { Text("著者") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = description.value,
                onValueChange = { newValue -> description.value = newValue },
                label = { Text("書籍概要") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = pageCount.value,
                onValueChange = { newValue -> description.value = newValue },
                label = { Text("総ページ数") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}