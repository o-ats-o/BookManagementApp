package com.example.bookmanagementapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookmanagementapp.ui.theme.BookManagementAppTheme
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isbn: String = ""
        val barcodeLauncher = registerForActivityResult (ScanContract()) { result ->
            if (result.contents == null) {
                android.widget.Toast.makeText(this, "Cancelled", android.widget.Toast.LENGTH_LONG)
                    .show()
            } else {
                if (result.contents.startsWith("978")) {
                    isbn = result.contents
                    android.widget.Toast.makeText(this, "ISBN: " + result.contents, android.widget.Toast.LENGTH_LONG)
                        .show()
                } else {
                    android.widget.Toast.makeText(this, "Not ISBN: ", android.widget.Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        fun onButtonClick() {
            val options = ScanOptions().setOrientationLocked(true)
            barcodeLauncher.launch(options)
        }

        super.onCreate(savedInstanceState)
        setContent {
            BookManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BarcodeReaderApp(onClickAction = {onButtonClick() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeReaderApp(onClickAction: () -> Unit) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickAction,
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