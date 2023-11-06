package com.example.bookmanagementapp.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookmanagementapp.R

@Composable
fun BookInfoScreen(
    bookUiState: BookUiState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    when(bookUiState) {
        is BookUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookUiState.Success -> ResultScreen(
            bookUiState.info, modifier = modifier.fillMaxWidth()
        )

        is BookUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Loading")
    }
}

@Composable
fun ResultScreen(info: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = info)
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Error")
    }
}

@Preview
@Composable
fun ResultScreenPreview() {
    ResultScreen("Success: 1 Book retrieved")
}