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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// 書籍情報のレイアウト
@Composable
fun BookInfoLayout(bookInfo: BookInfo?, modifier: Modifier = Modifier) {
    val title = remember { mutableStateOf(bookInfo?.title?: "") }
    val authors = remember { mutableStateOf(bookInfo?.authors?.joinToString(", ") ?: "") }
    val description = remember { mutableStateOf(bookInfo?.description?: "") }
    val pageCount = remember { mutableStateOf(bookInfo?.pageCount?.toString()?: "") }

    LazyColumn (
        modifier = modifier
            .padding(top = 24.dp, start = 18.dp, end = 18.dp)
    ) {
        item { BookCover(bookInfo?.imageLinks) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { BookField(title, "タイトル") { newValue -> title.value = newValue } }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { BookField(authors, "著者") { newValue -> authors.value = newValue } }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { BookField(description, "書籍概要") { newValue -> description.value = newValue } }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { BookField(pageCount, "総ページ数") { newValue -> pageCount.value = newValue } }
        item { Spacer(modifier = Modifier.height(38.dp)) }
        item { BookInfoSaveButton() }
    }
}

// 書籍の表紙画像
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

// 書籍情報のテキストフィールド
@Composable
fun BookField(
    value: MutableState<String>,
    label: String,
    onValueChange: (String) -> Unit
) {
    // テキストフィールドの入力状態を保持する
    val (text, setText) = remember { mutableStateOf(value.value) }
    Column {
        TextField(
            value = text,
            onValueChange = {
                setText(it)
                onValueChange(it)
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            trailingIcon = {
                if (text.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color.Red
                    )
                }
            },
            // テキストフィールドに1文字も入っていない時，テキストフィールドの下線を赤く，それ以外は元々の色
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (text.isEmpty()) Color.Red else Color.Gray,
                unfocusedIndicatorColor = if (text.isEmpty()) Color.Red else Color.Gray,
            )
        )

        // 入力が空の場合に警告メッセージを表示する
        if (text.isEmpty()) {
            Text(
                text = "1文字以上のテキストを入力してください",
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp),
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}

// 書籍情報保存ボタン
@Composable
fun BookInfoSaveButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = modifier
                .width(120.dp)
                .height(40.dp)
        ) {
            Text("保存")
        }
    }
}

// 書籍情報レイアウトのプレビュー
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