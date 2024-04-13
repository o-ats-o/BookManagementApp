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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
            if (result.contents.startsWith("978")) {
                val isbn = result.contents
                Toast.makeText(context, "Scanned ISBN: $isbn", Toast.LENGTH_LONG).show()
                // BookInfoScreenに遷移
                navController.navigate("bookInformation/$isbn")
            } else {
                Toast.makeText(
                    context,
                    "Not ISBN: ",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    Scaffold (
        floatingActionButton = {
            // スキャンオプションの設定
            val scanOptions = ScanOptions().apply {
                setOrientationLocked(false)
                setPrompt("バーコードを画面の読み取り範囲内に写すとスキャンします")
            }
            // フローティングアクションボタン
            FloatingActionButton(
                onClick = {
                    barcodeLauncher.launch(scanOptions)
                          },
                shape = MaterialTheme.shapes.medium,
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
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Barcode Reader App"
            )
        }
    }
}