package com.example.bookmanagementapp.view.screen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.bookmanagementapp.model.BookInfo
import com.example.bookmanagementapp.model.ImageLinks
import com.example.bookmanagementapp.viewmodel.BookInfoViewModel
import com.example.bookmanagementapp.viewmodel.BookInfoViewState

@Composable
fun BookInfoScreen(
    modifier: Modifier = Modifier,
    isbn: String,
    navController: NavHostController,
    viewModel: BookInfoViewModel = hiltViewModel(),
){
    val bookInfoState = viewModel.bookInfoState.collectAsState()
    // エラーメッセージを監視
    val errorMessage = viewModel.errorMessage.collectAsState()

    LaunchedEffect(isbn) {
        viewModel.getBookInfo(isbn)
    }

    val context = LocalContext.current
    // エラーメッセージが存在する場合、トーストを表示して画面遷移
    LaunchedEffect(errorMessage.value) {
        errorMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            navController.navigate("isbnScanner")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = bookInfoState.value) {
            is BookInfoViewState.Loading -> {
                // Progress varを表示
                CircularProgressIndicator()
            }
            is BookInfoViewState.Success -> {
                BookInfoLayout(
                    state.data,
                    onSaveBookInfo = {
                        viewModel.saveBookInfoToLocalDatabase(isbn ,state.data)
                        navController.navigate("isbnScanner")
                    }
                )
            }
            is BookInfoViewState.Error -> {
                // エラーメッセージをログに表示
                Log.e("BookInfoScreen", "Error: ${state.message}")
            }
        }
    }
}

// 書籍情報のレイアウト
@Composable
fun BookInfoLayout(
    bookInfo: BookInfo?,
    onSaveBookInfo: () -> Unit,
    modifier: Modifier = Modifier
) {

    // 書籍情報の各項目を保持する
    val title = remember { mutableStateOf(bookInfo?.title?: "") }
    val authors = remember { mutableStateOf(bookInfo?.authors?.joinToString(", ") ?: "") }
    val description = remember { mutableStateOf(bookInfo?.description?: "") }
    val pageCount = remember { mutableStateOf(bookInfo?.pageCount?.toString()?: "") }

    // 全てのテキストフィールドが空でないかどうか
    val isNotEmpty = title.value.isNotEmpty() && authors.value.isNotEmpty() && description.value.isNotEmpty() && pageCount.value.isNotEmpty()

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
        item { BookInfoSaveButton(isNotEmpty, onSaveBookInfo) }
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
    isNotEmpty: Boolean,
    onSaveBookInfo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { onSaveBookInfo() },
            modifier = modifier
                .width(120.dp)
                .height(40.dp),
            enabled = isNotEmpty
        ) {
            Text("保存")
        }
    }
}

// エラー画面のレイアウト
@Composable
fun ErrorLayout(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            modifier = modifier.padding(16.dp),
            fontSize = 16.sp
        )
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
            thumbnail = ""
        )
    )
    BookInfoLayout(bookInfo, onSaveBookInfo = {})
}

// エラーレイアウトのプレビュー
@Preview
@Composable
fun PreviewErrorLayout() {
    ErrorLayout("エラーが発生しました")
}