package com.example.bookmanagementapp.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BookInformationScreen(
    bookUiState: String, modifier: Modifier = Modifier
) {
    ResultScreen(bookUiState, modifier)
}

@Composable
fun ResultScreen(information: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = information)
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    ResultScreen("Hello World")
}