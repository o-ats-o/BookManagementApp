package com.example.bookmanagementapp.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.model.ImageLinks
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
        Column(modifier = modifier) {
            BookInfoLayout(bookInfo)
        }
    }
}

@Composable
fun BookInfoLayout(bookInfo: BookInfo?, modifier: Modifier = Modifier) {
    val title = remember { mutableStateOf(bookInfo?.title?: "") }
    val authors = remember { mutableStateOf(bookInfo?.authors?.joinToString(", ") ?: "") }
    val description = remember { mutableStateOf(bookInfo?.description?: "") }
    val pageCount = remember { mutableStateOf(bookInfo?.pageCount?.toString()?: "") }

    LazyColumn (
        modifier = modifier
            .padding(18.dp)
            .padding(top = 24.dp)
    ) {
        item { BookCover(bookInfo?.imageLinks) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { BookField(title, "タイトル") { newValue -> title.value = newValue } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { BookField(authors, "著者") { newValue -> authors.value = newValue } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { BookField(description, "書籍概要") { newValue -> description.value = newValue } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { BookField(pageCount, "総ページ数") { newValue -> pageCount.value = newValue } }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { BookInfoSaveButton() }
    }
}

@Composable
fun BookCover(
    imageLinks: ImageLinks?,
    modifier: Modifier = Modifier
) {
    imageLinks?.let { links ->
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = links.thumbnail).apply(block = fun ImageRequest.Builder.() {
                            transformations(RoundedCornersTransformation(10f))
                        }).build(),
                ),
                contentDescription = "Book cover image",
                modifier
                    .height(240.dp)
                    .width(160.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

        }
    }
}

@Composable
fun BookField(
    value: MutableState<String>,
    label: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value.value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BookInfoSaveButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier

    ) {
        Text("保存")
    }
}

@Preview
@Composable
fun PreviewBookInfoLayout() {
val bookInfo = BookInfo(
        title = "タイトル",
        authors = listOf("著者1", "著者2"),
        description = "書籍概要",
        pageCount = 100,
        imageLinks = ImageLinks(
            thumbnail = "http://books.google.com/books/content?id=L_ggEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        )
    )
    BookInfoLayout(bookInfo)
}