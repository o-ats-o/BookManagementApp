package com.example.bookmanagementapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagementapp.view.screen.BookInfoScreen
import com.example.bookmanagementapp.view.screen.BookListScreen
import com.example.bookmanagementapp.view.screen.ProgressInfoScreen
import com.example.bookmanagementapp.view.theme.BookManagementAppTheme
import com.example.bookmanagementapp.viewmodel.BookListViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
                        if (result.contents == null) {
                            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            if (result.contents.startsWith("97")) {
                                navController.navigate("bookInformation/${result.contents}")
                            } else {
                                Toast.makeText(this, "Invalid ISBN", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                    MainNavHost(navController = navController, barcodeLauncher = barcodeLauncher)
                }
            }
        }
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    barcodeLauncher: ActivityResultLauncher<ScanOptions>
) {
    val viewModel: BookListViewModel = hiltViewModel()
    val allBooks by viewModel.allBooks.collectAsState()

    NavHost(navController = navController, startDestination = "isbnScanner") {
        composable("isbnScanner") {
            IsbnScanner(navController)
        }
        composable("bookInformation/{isbn}") { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            BookInfoScreen(isbn = isbn, navController = navController, viewModel = hiltViewModel())
        }
        composable("bookList") {
            BookListScreen(
                navController = navController,
                barcodeLauncher = barcodeLauncher,
                viewModel = hiltViewModel()
            )
        }
        composable("progressInfo/{isbn}") { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            val book = allBooks.find { it.isbn == isbn }
            if (book != null) {
                ProgressInfoScreen(book, navController, viewModel = hiltViewModel())
            }
        }
    }
}


