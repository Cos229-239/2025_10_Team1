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
import com.example.myapplication.ui1.DropdownItem

@Composable
fun FocusScreen(navController: NavController, modifier: Modifier = Modifier) {
    // FIX: Define the standard gradient using the correct purple-to-teal colors.
    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    // Use a Box as the root layout to apply the background.
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(standardGradient) // Apply the standard gradient
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
                color = Color.White // Text is already white for readability
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.FocusSession.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA6E6DA) // This button color works well
                )
            ) {
                Text("Start a Focus Session", color = Color.DarkGray)
            }
        }

        // The TopAppBar floats on top of the gradient background.
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
                color = Color.White // Title is already white
            )
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(28.dp),
                        tint = Color.White // Icon is already white
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
