package com.example.bookmanagementapp.view.screen

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookmanagementapp.CustomScannerActivity
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.viewmodel.BookListViewModel
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    navController: NavController,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>
) {
    val readingProgress = viewModel.readingProgress.collectAsState()

    Scaffold(
        floatingActionButton = {
            val scanOptions = ScanOptions().apply {
                setCaptureActivity(CustomScannerActivity::class.java)
                setOrientationLocked(false)
                setPrompt("")
            }
            FloatingActionButton(
                onClick = { barcodeLauncher.launch(scanOptions) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Add, contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(readingProgress.value.size) { index ->
                val (book, progress) = readingProgress.value[index]

                BookItemCard(
                    book = book,
                    progress = progress,
                    modifier = Modifier.padding(8.dp),
                    onBookClick = {
                        navController.navigate("progressInfo/${book.isbn}")
                    }
                )
            }
        }
    }
}

@Composable
fun BookItemCard(
    modifier: Modifier = Modifier,
    book: BookInfoEntity,
    progress: Float,
    onBookClick: (BookInfoEntity) -> Unit = {}
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .clickable {
                onBookClick(book)
            }
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(model = book.thumbnail),
                contentDescription = "Book thumbnail",
                modifier = Modifier
                    .height(130.dp)
                    .width(90.dp)
                    .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
            )
            Column {
                Text(
                    text = book.title,
                    modifier = Modifier.padding(start = 6.dp, top = 9.dp, end = 6.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text( // 著者情報を表示するTextコンポーネントを追加
                    text = book.authors,
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 26.dp)
                        .align(Alignment.End)
                )
                // 進捗状況を示すLinearProgressIndicatorを追加
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 28.dp, top = 12.dp)
                        .height(7.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Gray,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBookItemCard(){
    BookItemCard(
        book = BookInfoEntity(
            isbn = "9784774194310",
            title = "タイトル",
            authors = "著者",
            description = "説明",
            pageCount = 100,
            thumbnail = "https://books.google.com/books/content?id=1l8qAQAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
            readPageCount = 100
        ),
        progress = 0.5f
    )
}