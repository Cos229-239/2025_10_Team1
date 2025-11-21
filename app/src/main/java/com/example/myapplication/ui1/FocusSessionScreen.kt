package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import com.example.myapplication.Screen


/* ---------- Focus Screen ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionScreen(
    vm: FocusSessionViewModel,
    navController: NavController? = null
) {
    val ui by vm.state.collectAsState()

    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TisenseHeader(onMenuClick = { navController?.navigate(Screen.Menu.route) }) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Banner(text = "It’s time to lock in!")

            GoalRow(goal = ui.progress)

            TimerWheelCard(
                minutes = ui.wheelMinutes,
                seconds = ui.wheelSeconds,
                isRunning = ui.isRunning,
                onMinutesChanged = vm::setWheelMinutes,
                onSecondsChanged = vm::setWheelSeconds
            )

            Text(
                text = "Current task:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            TaskPill(text = ui.currentTask)

            // Add Note + Break row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GhostButton(
                    text = "Add Note To Task",
                    modifier = Modifier.weight(1f)
                ) { showNoteDialog = true }

                SoftButton(
                    text = "Break",
                    bg = Color(0xFFE6F1C9),
                    modifier = Modifier.weight(1f)
                ) { vm.startBreak() }
            }

            // Full Focus + Silence Mode row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SoftButton(
                    text = "Full Focus",
                    bg = Color(0xFFE7C6F3),
                    modifier = Modifier.weight(1f)
                ) { vm.fullFocus() }

                SoftButton(
                    text = if (ui.isSilenceOn) "Silence: ON" else "Silence Mode",
                    bg = Color(0xFFF6D0AC),
                    modifier = Modifier.weight(1f)
                ) { vm.toggleSilence() }
            }

            // Start / Pause session button
            SoftButton(
                text = if (ui.isRunning) "Pause Session" else "Start Session",
                bg = Color(0xFFFFF4A6),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) { vm.toggleRun() }

// Reset timer button
            GhostButton(
                text = "Reset Timer",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) { vm.resetSession() }

// Notes list (optional)
            if (ui.notes.isNotEmpty()) {

                Text(
                    text = "Notes:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ui.notes.forEachIndexed { index, note ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF1F1F1))
                                .clickable {
                                    navController?.navigate("note/${note}")
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. $note",
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { vm.deleteNoteAt(index) }) {
                                Text("Delete", color = Color.Red)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.addNote(noteText)
                        noteText = ""
                        showNoteDialog = false
                    }
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Add Note") },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Enter your note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
        )
    }
}

/* ---------- Note Detail Screen ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(note: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = note,
                fontSize = 20.sp,
                color = Color(0xFF333333),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


/* ---------- Supporting UI Components ---------- */

@Composable
private fun Banner(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF2D6F1))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF303030)
        )
    }
}

@Composable
private fun GoalRow(goal: Float) {
    val clamped = goal.coerceIn(0f, 1f)
    val percentage = (clamped * 100).toInt()

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Todays Goal",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$percentage%",
                fontSize = 18.sp
            )
        }

        LinearProgressIndicator(
            progress = clamped,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp)),
            color = Color(0xFF9BDAD4),
            trackColor = Color(0xFFE5E5E5)
        )
    }
}

@Composable
private fun TimerWheelCard(
    minutes: Int,
    seconds: Int,
    isRunning: Boolean,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFFF4D2A9))
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        TimerWheel(
            minutes = minutes,
            seconds = seconds,
            enabled = !isRunning,
            onMinutesChanged = onMinutesChanged,
            onSecondsChanged = onSecondsChanged
        )
    }
}

@Composable
private fun TaskPill(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFCBEFEA)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 18.dp),
            fontSize = 20.sp,
            color = Color(0xFF0E3131),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SoftButton(
    text: String,
    bg: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 18.sp,
            color = Color(0xFF9A9A9A),
            textAlign = TextAlign.Center
        )
    }
}
