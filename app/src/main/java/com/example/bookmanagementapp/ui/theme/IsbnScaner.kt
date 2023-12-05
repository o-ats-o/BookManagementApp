@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookmanagementapp.ui.theme

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
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

var isbn = ""
@Composable
fun IsbnScanner(navController: NavController) {
    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG)
                .show()
        } else {
            if (result.contents.startsWith("978")) {
                isbn = result.contents
                navController.navigate("bookInfo")
            } else {
                Toast.makeText(
                    context,
                    "NOT ISBN: ",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { barcodeLauncher.launch(ScanOptions()) },
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