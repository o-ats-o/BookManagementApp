@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagementapp.ui.theme.BookManagementAppTheme
import com.example.bookmanagementapp.ui.theme.IsbnScanner
import com.example.bookmanagementapp.ui.theme.screens.BookInfoScreen
import com.example.bookmanagementapp.ui.theme.screens.BookUiState
import com.example.bookmanagementapp.ui.theme.screens.BookViewModel

class MainActivity : ComponentActivity() {
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

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "isbnScanner") {
        composable("isbnScanner") {
            IsbnScanner( navController)
        }
        composable("bookInformation") {
            val bookViewModel: BookViewModel = viewModel()
            BookInfoScreen( BookUiState.Loading)
        }
    }
}


