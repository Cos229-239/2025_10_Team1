package com.example.myapplication.ui.log


import android.util.Log

import com.example.myapplication.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme


data class RecentLog(val title: String, val color: Color)
// --- UPDATE: Added a text color property for better contrast ---
data class DropdownItem(val text: String, val backgroundColor: Color, val textColor: Color)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(userName: String, modifier: Modifier = Modifier) {
    val recentLogs = remember {
        mutableStateListOf(
            RecentLog("Too Tired", Color(0xFFEAFDE9)),
            RecentLog("Too Many Distractions", Color(0xFFFFEFCB)),
            RecentLog("Lacking Motivations", Color(0xFFF8E8FF))
        )
    }
    var newLogText by remember { mutableStateOf("") }
    val logColors = listOf(
        Color(0xFFEAFDE9), // Light Green
        Color(0xFFFFEFCB), // Light Orange
        Color(0xFFF8E8FF), // Light Purple
        Color(0xFFE0F7FA), // Light Cyan
        Color(0xFFFFEBEE)  // Light Pink
    )

    Scaffold(
        topBar = { LogTopAppBar() },
        bottomBar = { LogBottomNavigationBar() },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LogInputSection(
                    userName = userName,
                    text = newLogText,
                    onTextChange = { newLogText = it },
                    onLogSubmitted = {
                        if (newLogText.isNotBlank()) {
                            recentLogs.add(0, RecentLog(newLogText, logColors.random()))
                            newLogText = ""
                        }
                    }
                )
            }
            item {
                Text(
                    text = "Recent Logs",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(recentLogs) { log ->
                RecentLogItem(log = log)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTopAppBar() {
    var menuExpanded by remember { mutableStateOf(false) }
    // --- UPDATE: Using new DropdownItem structure with background and text colors ---
    val menuItems = listOf(
        DropdownItem("Focus", backgroundColor = Color(0xFFE8F5E9), textColor = Color(0xFF2E7D32)),       // Light Green / Dark Green
        DropdownItem("Breakroom", backgroundColor = Color(0xFFE3F2FD), textColor = Color(0xFF1565C0)), // Light Blue / Dark Blue
        DropdownItem("Insights", backgroundColor = Color(0xFFFFF8E1), textColor = Color(0xFFF9A825)),  // Light Amber / Dark Amber
        DropdownItem("Planner", backgroundColor = Color(0xFFF3E5F5), textColor = Color(0xFF6A1B9A)),   // Light Purple / Dark Purple
        DropdownItem("Home", backgroundColor = Color(0xFFFFEBEE), textColor = Color(0xFFC62828)),         // Light Red / Dark Red
        DropdownItem("Menu", backgroundColor = Color(0xFFECEFF1), textColor = Color(0xFF37474F))          // Light Grey / Dark Grey
    )

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "My Log",
                    style = MaterialTheme.typography.headlineMedium
                )
                Icon(
                    painter = painterResource(id = R.drawable.tisense_icon_2),
                    contentDescription = "App Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(54.dp)
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(28.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                // --- UPDATE: Creating the oval bubble ---
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50)) // Makes it an oval/pill shape
                                        .background(item.backgroundColor)
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(item.text, color = item.textColor)
                                }
                            },
                            onClick = {
                                Log.d("LogScreen", "${item.text} clicked")
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun LogInputSection(
    userName: String,
    text: String,
    onTextChange: (String) -> Unit,
    onLogSubmitted: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What's stopping you\nright now, $userName?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Write a quick note...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogSubmitted,
            modifier = Modifier.align(Alignment.End),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA6E6DA)
            )
        ) {
            Text("Log", color = Color.DarkGray)
        }
    }
}

@Composable
fun RecentLogItem(log: RecentLog) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "rotationAnimation"
    )

    // --- AI ATTRIBUTION ---
    // The following function provides mock recommendations for demonstration purposes.
    // In a real application, this logic would be replaced by a call to a generative
    // AI model like Google's Gemini API.
    fun getAiRecommendation(title: String): String {
        return when {
            "tired" in title.lowercase() -> "Try the 5-minute rule: work on a task for just 5 minutes. Often, starting is the hardest part. A short walk can also boost energy."
            "distractions" in title.lowercase() -> "Consider using 'Focus Mode' on your phone. Put on headphones, even with no music, to signal to others that you're busy."
            "motivation" in title.lowercase() -> "Break down your main goal into smaller, more manageable tasks. Celebrate completing each small step to build momentum."
            else -> "A great first step is to take a deep breath. Stand up, stretch, and get a glass of water. A brief reset can do wonders for your focus and clarity."
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (rotation < 90f) {
            CardFront(log)
        } else {
            CardBack(log, getAiRecommendation(log.title))
        }
    }
}

@Composable
fun CardFront(log: RecentLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(log.color)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = log.title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand Log"
        )
    }
}

@Composable
fun CardBack(log: RecentLog, recommendation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(log.color)
            .graphicsLayer {
                rotationY = 180f
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Recommendation",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = recommendation,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}


data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun LogBottomNavigationBar() {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, "home"),
        BottomNavItem("Log", Icons.Filled.Edit, "log"),
        BottomNavItem("Planner", Icons.Filled.DateRange, "planner"),
        BottomNavItem("Insights", Icons.Filled.Info, "insights"),
        BottomNavItem("Account", Icons.Filled.AccountCircle, "account")
    )
    var selectedItem by remember { mutableIntStateOf(1) } // "Log" is selected

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    Log.d("LogScreen", "Navigate to: ${item.route}")
                }
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun LogScreenPreview() {
    MyApplicationTheme {
        LogScreen(userName = "Name")
    }
}
