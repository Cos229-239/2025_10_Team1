package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FocusSessionScreen(
    navController: NavController,
    viewModel: FocusSessionViewModel = viewModel()
) {
    // Collect the UI state from the ViewModel. This makes the UI reactive.
    val uiState by viewModel.state.collectAsState()
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    val neutralGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF5F5F5), Color.White)
    )

    Scaffold(
        topBar = {
            // Use the shared AppHeader for a consistent look and back navigation.
            AppHeader(
                title = "Focus Session",
                navController = navController
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(neutralGradient)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Banner(text = "It’s time to lock in!")

            // UI elements now read their values directly from uiState
            GoalRow(goal = uiState.progress)

            TimerWheelCard(
                minutes = uiState.wheelMinutes,
                seconds = uiState.wheelSeconds,
                isRunning = uiState.isRunning,
                onMinutesChanged = viewModel::setWheelMinutes,
                onSecondsChanged = viewModel::setWheelSeconds
            )

            Text(
                text = "Current task:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            TaskPill(text = uiState.currentTask)

            // --- Buttons with Corrected onClick Actions ---

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GhostButton(
                    text = "Add Note To Task",
                    modifier = Modifier.weight(1f),
                    onClick = { showNoteDialog = true }
                )
                SoftButton(
                    text = "Break",
                    bg = Color(0xFFE6F1C9),
                    modifier = Modifier.weight(1f),
                    // FIX: Call the startBreak function on the ViewModel
                    onClick = { viewModel.startBreak() }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SoftButton(
                    text = "Full Focus",
                    bg = Color(0xFFE7C6F3),
                    modifier = Modifier.weight(1f),
                    // FIX: Call the fullFocus function on the ViewModel
                    onClick = { viewModel.fullFocus() }
                )
                SoftButton(
                    text = if (uiState.isSilenceOn) "Silence: ON" else "Silence Mode",
                    bg = Color(0xFFF6D0AC),
                    modifier = Modifier.weight(1f),
                    // FIX: Call the toggleSilence function on the ViewModel
                    onClick = { viewModel.toggleSilence() }
                )
            }

            SoftButton(
                // Text changes based on whether the timer is running
                text = if (uiState.isRunning) "Pause Session" else "Start Session",
                bg = Color(0xFFFFF4A6),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                // FIX: Call the toggleRun function on the ViewModel
                onClick = { viewModel.toggleRun() }
            )

            GhostButton(
                text = "Reset Timer",
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                // FIX: Call the resetSession function on the ViewModel
                onClick = { viewModel.resetSession() }
            )

            // --- Notes Section ---
            if (uiState.notes.isNotEmpty()) {
                Text(
                    text = "Notes:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.notes.forEachIndexed { index, note ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${index + 1}. $note", modifier = Modifier.weight(1f))
                            TextButton(onClick = { viewModel.deleteNoteAt(index) }) {
                                Text("Delete", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    // --- Add Note Dialog ---
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // FIX: Call the addNote function on the ViewModel
                        viewModel.addNote(noteText)
                        noteText = "" // Clear text field
                        showNoteDialog = false
                    }
                ) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showNoteDialog = false }) { Text("Cancel") } },
            title = { Text("Add Note") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Enter your note") },
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                )
            }
        )
    }
}


/* ---------- Supporting UI Components (No Changes Needed Here) ---------- */

@Composable
private fun Banner(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0xFFF2D6F1)).padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF303030))
    }
}

@Composable
private fun GoalRow(goal: Float) {
    val clamped = goal.coerceIn(0f, 1f)
    val percentage = (clamped * 100).toInt()
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Todays Goal", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "$percentage%", fontSize = 18.sp)
        }
        LinearProgressIndicator(progress = { clamped }, modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(999.dp)), color = Color(0xFF9BDAD4), trackColor = Color(0xFFE5E5E5))
    }
}

@Composable
private fun TimerWheelCard(minutes: Int, seconds: Int, isRunning: Boolean, onMinutesChanged: (Int) -> Unit, onSecondsChanged: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(Color(0xFFF4D2A9)).padding(vertical = 20.dp, horizontal = 16.dp)) {
        TimerWheel(minutes = minutes, seconds = seconds, enabled = !isRunning, onMinutesChanged = onMinutesChanged, onSecondsChanged = onSecondsChanged)
    }
}

@Composable
private fun TaskPill(text: String) {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFCBEFEA)), contentAlignment = Alignment.Center) {
        Text(text = text, modifier = Modifier.padding(vertical = 18.dp), fontSize = 20.sp, color = Color(0xFF0E3131), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SoftButton(text: String, bg: Color, modifier: Modifier = Modifier, onClick: (() -> Unit)?) {
    // The implementation of SoftButton was already correct, using .clickable to handle the onClick lambda.
    Box(
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(bg).clickable(enabled = onClick != null) { onClick?.invoke() }.padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun GhostButton(text: String, modifier: Modifier = Modifier, onClick: (() -> Unit)?) {
    // The implementation of GhostButton was also correct.
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp)).clickable(enabled = onClick != null) { onClick?.invoke() }.padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 18.sp, color = Color(0xFF9A9A9A), textAlign = TextAlign.Center)
    }
}


