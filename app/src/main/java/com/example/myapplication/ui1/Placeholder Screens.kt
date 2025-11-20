package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


// --- EDIT PROFILE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    // FIX 1: Define the standard gradient
    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = Color.White) }, // FIX 2: Set text color
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, "Back", tint = Color.White) // FIX 3: Set icon tint
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent) // Make TopAppBar transparent
            )
        },
        containerColor = Color.Transparent // Make Scaffold transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(standardGradient) // Apply gradient to the Box
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Edit Profile Screen",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White // FIX 4: Set text color
            )
        }
    }
}


// --- Placeholder Screens ---
// NOTE: These placeholder screens conflict with real screens defined elsewhere.
// They are updated here for consistency, but should likely be removed or renamed.

@Composable
fun LogScreen(navController: NavController) {
    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(standardGradient) // Apply gradient
            .padding(horizontal = 16.dp)
    ) {
        // AppHeader is already transparent and works well on a dark background.
        AppHeader(title = "Log", navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Log Screen", color = Color.White) // Set text color
        }
    }
}

@Composable
fun AccountScreen(navController: NavController) {
    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(standardGradient) // Apply gradient
            .padding(horizontal = 16.dp)
    ) {
        // AppHeader is already transparent.
        AppHeader(title = "Account", navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Account Screen", color = Color.White) // Set text color
        }
    }
}

