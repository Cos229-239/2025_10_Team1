package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                MyApplicationTheme {
                    PlannerScreen(modifier = Modifier.fillMaxSize())
                }
                }
            }
        }
    }
}

@Composable
fun PlannerScreen(modifier: Modifier = Modifier) {
    // Scaffold provides the basic material design layout structure
    Scaffold(
        bottomBar = {
            // We'll define the BottomNavigationBar later
        },
        modifier = modifier
    ) { innerPadding ->
        // Main content goes here, using the padding from the Scaffold
        Column(
            modifier = Modifier
                .padding(innerPadding) // Important for content not to overlap with bars
                .padding(horizontal = 16.dp) // Add horizontal padding to the whole screen
        ) {
            // All the screen components will be called here
            TopHeader()
            WelcomeMessage(name = "Name")
            DaySelector()
        }
    }
}

@Composable
fun DaySelector() {
    TODO("Not yet implemented")
}

@Composable
fun WelcomeMessage(name: String) {
    TODO("Not yet implemented")
}

@Composable
fun TopHeader() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        PlannerScreen()
    }
}
