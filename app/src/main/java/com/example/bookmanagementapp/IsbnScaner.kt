package com.example.bookmanagementapp

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookmanagementapp.view.screen.BookListScreen
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun IsbnScanner(navController: NavController) {
    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG)
                .show()
        } else {
            if (result.contents.startsWith("97")) {
                navController.navigate("bookInformation/${result.contents}")
            } else {
                Toast.makeText(context, "Invalid ISBN", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    Scaffold (
        floatingActionButton = {
            // スキャンオプションの設定
            val scanOptions = ScanOptions().apply {
                setCaptureActivity(CustomScannerActivity::class.java)
                setOrientationLocked(false)
                setPrompt("")
            }
            // フローティングアクションボタン
            FloatingActionButton(
                onClick = {
                    barcodeLauncher.launch(scanOptions)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Add, contentDescription = "Add"
                )
            }
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BookListScreen()
        }
    }
}