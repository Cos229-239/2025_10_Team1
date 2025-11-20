package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Screen
// FIX 1: Import the shared DropdownItem from its central location.
import com.example.myapplication.ui1.DropdownItem

// FIX 2: The local definition of DropdownItem has been removed from here.
// private data class DropdownItem(val text: String, val route: String) // <-- This line is deleted.

@Composable
fun FocusScreen(navController: NavController, modifier: Modifier = Modifier) {
    // 1. Define a local gradient, following the LogScreen pattern.
    val focusGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1D2951), Color(0xFF2C4079)) // Dark Blue gradient
    )

    // 2. Use a Box as the root layout, just like LogScreen.
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(focusGradient)
    ) {
        // Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ready to enter deep focus?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White // Set text to white for readability on dark background
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    // Use your project's Screen sealed class for navigation
                    navController.navigate(Screen.FocusSession.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA6E6DA) // Same style as LogScreen button
                )
            ) {
                Text("Start a Focus Session", color = Color.DarkGray)
            }
        }

        // 3. Place the custom TopAppBar inside the Box, floating on top.
        FocusTopAppBar(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FocusTopAppBar(navController: NavController) {
    var menuExpanded by remember { mutableStateOf(false) }

    // Define menu items locally
    val menuItems = listOf(
        DropdownItem("Home", Screen.Home.route),
        DropdownItem("Log", Screen.Log.route),
        DropdownItem("Breakroom", Screen.Breakroom.route),
        DropdownItem("Insights", Screen.Insights.route),
        DropdownItem("Planner", Screen.Planner.route),
        DropdownItem("Menu", Screen.Menu.route)
    )

    TopAppBar(
        title = {
            Text(
                text = "Focus Mode",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White // White title for readability
            )
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(28.dp),
                        tint = Color.White // White icon for readability
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.text) },
                            onClick = {
                                navController.navigate(item.route)
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        },
        // Make app bar transparent to show the gradient
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
