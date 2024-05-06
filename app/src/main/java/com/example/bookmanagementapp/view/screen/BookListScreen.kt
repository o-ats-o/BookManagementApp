package com.example.bookmanagementapp.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookmanagementapp.model.BookInfoEntity
import com.example.bookmanagementapp.viewmodel.BookInfoViewModel

@Composable
fun BookListScreen(
    viewModel: BookInfoViewModel = hiltViewModel(),
    navController: NavController
) {
    val allBooks = viewModel.allBooks.collectAsState()

    LazyColumn {
        items(allBooks.value.size) { index ->
            val book = allBooks.value[index]
            BookItemCard(
                book = book,
                modifier = Modifier.padding(8.dp),
                onBookClick = {
                    navController.navigate("progressInfo/${book.isbn}")
                }
            )
        }
    }
}

@Composable
fun BookItemCard(
    modifier: Modifier = Modifier,
    book: BookInfoEntity,
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
                    .height(100.dp)
                    .width(80.dp)
                    .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
            )

            Column {
                Text(
                    text = book.title.toString(),
                    modifier = Modifier.padding(start = 6.dp, top = 6.dp),
                    fontSize = 18.sp
                )
                Text( // 著者情報を表示するTextコンポーネントを追加
                    text = book.authors.toString(),
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 14.sp
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
            readPageCount = 50
        )
    )
}