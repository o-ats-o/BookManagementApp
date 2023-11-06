@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookmanagementapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagementapp.ui.theme.BookManagementAppTheme
import com.example.bookmanagementapp.ui.theme.IsbnScanner
import com.example.bookmanagementapp.ui.theme.screens.BookInfoScreen
import com.example.bookmanagementapp.ui.theme.screens.BookViewModel

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainNavHost(navController = navController)
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "isbnScanner") {
        composable(route = "isbnScanner") {
            IsbnScanner(navController)
        }
        composable(route = "bookInfo") {
            val viewModel: BookViewModel = viewModel()
            BookInfoScreen(bookUiState = viewModel.bookUiState, navController)
        }
    }
}


