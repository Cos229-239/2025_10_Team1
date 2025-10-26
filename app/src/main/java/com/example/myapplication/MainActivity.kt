package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.log.LogScreen // <-- This line imports your new screen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // We are calling the new LogScreen here instead of the old "Greeting"
                LogScreen(
                    userName = "Name", // You can change this to a dynamic name
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
