package com.example.piecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.piecode.ui.theme.PieCodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PieCodeTheme {
                MainScreen() // this is here because you want to run this
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PieCode") } // Title for the navigation bar
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        CodeEditor(Modifier.padding(paddingValues)) // Adds padding so text isn't under the navbar
    }
}

@Composable
fun CodeEditor(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("hi") } // Stores the text input and makes it editable
    BasicTextField(
        value = text,
        onValueChange = { text = it }, // Updates the text as the user types
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun CodeEditorPreview() { // this is a function that shows the preview
    MainScreen()
   // CodeEditor() // this is the function that is shown in the preview
}
