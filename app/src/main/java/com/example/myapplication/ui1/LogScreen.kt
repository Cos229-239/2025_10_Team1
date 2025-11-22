package com.example.myapplication.ui1

import android.util.Log
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.Screen

// Data classes
data class RecentLog(val title: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(navController: NavController, userName: String, modifier: Modifier = Modifier) {
    val recentLogs = remember {
        mutableStateListOf(
            RecentLog("Too Tired", Color(0xFFEAFDE9)),
            RecentLog("Too Many Distractions", Color(0xFFFFEFCB)),
            RecentLog("Lacking Motivations", Color(0xFFF8E8FF))
        )
    }
    var newLogText by remember { mutableStateOf("") }
    val logColors = listOf(
        Color(0xFFEAFDE9), Color(0xFFFFEFCB), Color(0xFFF8E8FF),
        Color(0xFFE0F7FA), Color(0xFFFFEBEE)
    )

    val logGradient: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    Scaffold(
        topBar = {
            TisenseHeader(
                title = "My Log",
                onMenuClick = { navController.navigate(Screen.Menu.route) }
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier
            .fillMaxSize()
            .background(logGradient)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Use padding from Scaffold
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Recent Logs",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            items(recentLogs) { log ->
                RecentLogItem(log = log, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }
    }
}

@Composable
fun LogInputSection(
    userName: String,
    text: String,
    onTextChange: (String) -> Unit,
    onLogSubmitted: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = "What's stopping you\nright now, $userName?",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Write a quick note...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White.copy(alpha = 0.8f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
            )
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
fun RecentLogItem(log: RecentLog, modifier: Modifier = Modifier) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "rotationAnimation"
    )
    val density = LocalDensity.current

    fun getAiRecommendation(title: String): String {
        return when {
            "tired" in title.lowercase() -> "Try the 5-minute rule: work on a task for just 5 minutes. Often, starting is the hardest part. A short walk can also boost energy."
            "distractions" in title.lowercase() -> "Consider using 'Focus Mode' on your phone. Put on headphones, even with no music, to signal to others that you're busy."
            "motivation" in title.lowercase() -> "Break down your main goal into smaller, more manageable tasks. Celebrate completing each small step to build momentum."
            else -> "A great first step is to take a deep breath. Stand up, stretch, and get a glass of water. A brief reset can do wonders for your focus and clarity."
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density.density
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
            .graphicsLayer { rotationY = 180f }
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
