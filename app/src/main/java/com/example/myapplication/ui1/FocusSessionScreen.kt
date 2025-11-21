package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun FocusSessionScreen(
) {
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Banner(text = "It’s time to lock in!")


            TimerWheelCard(
            )

            Text(
                text = "Current task:",
                fontSize = 18.sp,
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GhostButton(
                    text = "Add Note To Task",
                SoftButton(
                    text = "Break",
                    bg = Color(0xFFE6F1C9),
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SoftButton(
                    text = "Full Focus",
                    bg = Color(0xFFE7C6F3),
                SoftButton(
                    bg = Color(0xFFF6D0AC),
            }

            SoftButton(
                bg = Color(0xFFFFF4A6),

            GhostButton(
                text = "Reset Timer",

                Text(
                    text = "Notes:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                        noteText = ""
                        showNoteDialog = false
                    }
                ) { Text("Save") }
            },
            title = { Text("Add Note") },
            text = {
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Enter your note") },
                )
            }
        )
    }
}


/* ---------- Supporting UI Components ---------- */

@Composable
private fun Banner(text: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
    }
}

@Composable
private fun GoalRow(goal: Float) {
    val clamped = goal.coerceIn(0f, 1f)
    val percentage = (clamped * 100).toInt()
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

@Composable
    }
}

@Composable
private fun TaskPill(text: String) {
    }
}

@Composable
    Box(
        contentAlignment = Alignment.Center
    ) {
    }
}

@Composable
    Box(
        contentAlignment = Alignment.Center
    ) {
    }
}
