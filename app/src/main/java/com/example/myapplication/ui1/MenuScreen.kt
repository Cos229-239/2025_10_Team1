package com.example.myapplication.ui1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.MenuItem
import com.example.myapplication.Screen

// --- MENU SCREEN ---

@Composable
fun MenuScreen(navController: NavController) {
    val menuItems = listOf(
        MenuItem("Home", Color(0xFFADD8E6), Screen.Home.route),
        MenuItem("My Log", Color(0xFFF0E68C), Screen.Log.route),
        MenuItem("Insights", Color(0xFFD8BFD8), Screen.Insights.route),
        MenuItem("Breakroom", Color(0xFFFFDAB9), Screen.Breakroom.route),
        MenuItem("Planner", Color(0xFFC1E1C1), Screen.Planner.route),
        MenuItem("Focus Session", Color(0xFFADD8E6), Screen.Home.route),
        MenuItem("Account", Color(0xFFF0E68C), Screen.Account.route),
        MenuItem("Settings", Color(0xFFD8BFD8), Screen.Settings.route)
    )

    Scaffold(
        topBar = { TisenseHeader(onMenuClick = { navController.navigate(Screen.Menu.route) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            menuItems.forEach { item ->
                MenuButton(
                    item = item,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun MenuButton(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = item.color.copy(alpha = 0.8f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
