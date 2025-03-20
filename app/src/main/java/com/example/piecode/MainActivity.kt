package com.example.piecode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.piecode.ui.theme.PieCodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Chaquopy
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        enableEdgeToEdge()
        setContent {
            PieCodeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "editor") {
                    composable("editor") { MainScreen(navController) }
                    composable("result/{output}") { backStackEntry ->
                        ResultScreen(
                            output = backStackEntry.arguments?.getString("output") ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PieCode - Python Editor") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4374D1),
                    titleContentColor = Color.White
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        CodeEditor(
            modifier = Modifier.padding(paddingValues),
            onExecute = { code ->
                val output = executePythonCode(code)
                navController.navigate("result/$output")
            }
        )
    }
}

@Composable
fun CodeEditor(modifier: Modifier = Modifier, onExecute: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
        )

        Button(
            onClick = { onExecute(text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4374D1))
        ) {
            Text("Run Code", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(output: String, navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Output") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4374D1),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Text("< Back", color = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Text(
            text = output,
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp)
        )
    }
}

private fun executePythonCode(code: String): String {
    val python = Python.getInstance()
    val io = python.getModule("io")
    val console = python.getModule("sys")

    val stdoutIO = io.callAttr("StringIO")
    console["stdout"] = stdoutIO

    val stderrIO = io.callAttr("StringIO")
    console["stderr"] = stderrIO

    val interpreter = python.getModule("code").callAttr("InteractiveInterpreter")

    val result = interpreter.callAttr("runsource", code, "<stdin>", "exec").toBoolean()

    val stdoutOutput = stdoutIO.callAttr("getvalue").toString().trim()
    val stderrOutput = stderrIO.callAttr("getvalue").toString().trim()

    return when {
        result -> "Syntax Error: Incomplete code (check indentation or missing closing statements)"
        stderrOutput.isNotEmpty() -> stderrOutput // Raw error output
        stdoutOutput.isNotEmpty() -> stdoutOutput
        else -> "No output"
    }
}

@Preview(showBackground = true)
@Composable
fun CodeEditorPreview() {
    PieCodeTheme {
        MainScreen(rememberNavController())
    }
}