package com.example.bookmanagementapp.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.viewmodel.ProgressInfoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressInfoScreen(
    book: BookInfoEntity,
    navController: NavController,
    viewModel: ProgressInfoViewModel = hiltViewModel()
) {
    var readPageCount by remember { mutableStateOf(book.readPageCount) }
    val pageCount by remember { mutableStateOf(book.pageCount) }
    var isNotEmpty by remember { mutableStateOf(false) }

    LaunchedEffect(readPageCount, pageCount) {
        isNotEmpty =
            (readPageCount)!! <= ((pageCount!!))
    }

    ProgressLayout(
        book = book,
        onSaveBookInfo = { newReadPageCount: Int ->
            viewModel.viewModelScope.launch {
                viewModel.updateBookProgress(
                    book.isbn,
                    newReadPageCount,
                    pageCount!!.toInt()
                )
                delay(1200)
                navController.navigate("bookList")
            }
        },
        onReadPageCountChange = { newReadPageCount: Int ->
            readPageCount = newReadPageCount
        },
        isNotEmpty = isNotEmpty
    )
}

@Composable
fun ProgressLayout(
    book: BookInfoEntity,
    onSaveBookInfo: (Int) -> Unit,
    onReadPageCountChange: (Int) -> Unit,
    isNotEmpty: Boolean
) {
    var readPageCount by remember { mutableStateOf(book.readPageCount.toString()) }
    var pageCount by remember { mutableStateOf(book.pageCount.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(book.thumbnail)
                    .transformations(RoundedCornersTransformation(4f))
                    .build()
            ),
            contentDescription = "Book thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(240.dp)
                .width(160.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.padding(top = 28.dp))
        Text(
            text = book.title.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(top = 2.dp))
        Text(
            text = book.authors.toString(),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.padding(top = 18.dp))
        TextField(
            value = readPageCount,
            onValueChange = {
                readPageCount = it
                onReadPageCountChange(it.toInt())
            },
            label = { Text("現在のページ数") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(top = 28.dp))
        TextField(
            value = pageCount,
            onValueChange = { pageCount = it },
            label = { Text("総ページ数") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(top = 60.dp))
        ProgressInfoSaveButton(
            onSaveBookInfo = { onSaveBookInfo(readPageCount.toInt())},
            isNotEmpty = isNotEmpty,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ProgressInfoSaveButton(
    modifier: Modifier = Modifier,
    onSaveBookInfo: () -> Unit,
    isNotEmpty: Boolean
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

@Preview
@Composable
fun PreviewProgressInfoLayout() {
    ProgressLayout(
        book = BookInfoEntity(
            isbn = "978-4-7741-9231-1",
            title = "Kotlinスタートブック",
            authors = "渡辺 竜王",
            description = "Kotlinの基本から応用までを網羅した解説書",
            pageCount = 360,
            thumbnail = "https://example.com/kotlin_start_book.jpg",
            readPageCount = 100
        ),
        onSaveBookInfo = {},
        isNotEmpty = true,
        onReadPageCountChange = {}
    )
}