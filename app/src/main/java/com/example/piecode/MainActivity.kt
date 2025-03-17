package com.example.piecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
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
                CodeEditor() // this is here because you want to run this
            }
        }
    }
}

@Composable
fun CodeEditor(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("hi") } // Stores the text input and makes it editable
    BasicTextField(
        value = text,
        onValueChange = { text = it }, // Updates the text as the user types
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun CodeEditorPreview() { // this is a function that shows the preview
    CodeEditor() // this is the function that is shown in the preview
}
