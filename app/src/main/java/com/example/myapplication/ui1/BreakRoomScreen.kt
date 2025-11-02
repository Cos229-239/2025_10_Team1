package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.AppHeader // Corrected import
import com.example.myapplication.Screen // Corrected import
import kotlinx.coroutines.delay

// --- BREAKROOM SCREEN ---
@Composable
fun BreakroomScreen(navController: NavController, modifier: Modifier = Modifier) {
    val initialTotalTimeSeconds = 5 * 60L
    var remainingTimeSeconds by remember { mutableStateOf(initialTotalTimeSeconds) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isRunning) {
        if (isRunning) {
            while (remainingTimeSeconds > 0) {
                delay(1000L)
                remainingTimeSeconds--
            }
            isRunning = false
            remainingTimeSeconds = initialTotalTimeSeconds
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFA4EBF3), Color(0xFFD4F1F4))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(title = "Breakroom", navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Let's take a break, Name.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(32.dp))
        BreakTimer(
            remainingTimeSeconds = remainingTimeSeconds,
            isRunning = isRunning,
            onToggle = { isRunning = !isRunning }
        )
        Spacer(modifier = Modifier.height(32.dp))
        BreathingGuide()
        Spacer(modifier = Modifier.weight(1f))
        BreakActivities(navController = navController)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// --- BREAKROOM COMPOSABLES ---

@Composable
fun BreakTimer(
    remainingTimeSeconds: Long,
    isRunning: Boolean,
    onToggle: () -> Unit
) {
    val minutes = (remainingTimeSeconds / 60).toString().padStart(2, '0')
    val seconds = (remainingTimeSeconds % 60).toString().padStart(2, '0')
    val timeString = "$minutes:$seconds"

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .clickable(onClick = onToggle)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                style = Stroke(width = 15.dp.toPx())
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timeString,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp
            )
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isRunning) "Pause Timer" else "Start Timer",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun BreathingGuide() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Air,
            contentDescription = "Breathe Icon",
            modifier = Modifier.size(48.dp),
            tint = Color.DarkGray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Inhale",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun BreakActivities(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActivityButton(
                text = "Breathe",
                icon = Icons.Outlined.Cloud,
                color = Color(0xFFFFF9C4),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate(Screen.Breathing.route) }
            )
            ActivityButton(
                text = "Stretch",
                icon = Icons.Outlined.AccessibilityNew,
                color = Color(0xFFF3E5F5),
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate(Screen.Stretch.route) }
            )
        }
        // This button now navigates to the music screen
        ActivityButton(
            text = "Play Music",
            icon = Icons.Outlined.MusicNote,
            color = Color(0xFFFFE0B2),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable { navController.navigate(Screen.Music.route) }
        )
    }
}

@Composable
fun ActivityButton(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}
