package com.example.myapplication.ui1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.myapplication.ui1.AppHeader
import com.example.myapplication.PlannerTask
import com.example.myapplication.TaskPriority

// --- PLANNER SCREEN ---
@Composable
fun PlannerScreen(
    navController: NavController,
    initialTasks: List<PlannerTask>
) {
    val tasks = remember { mutableStateListOf(*initialTasks.toTypedArray()) }
    var showDialog by remember { mutableStateOf(false) }

    fun onAddTask(title: String, priority: TaskPriority) {
        tasks.add(
            PlannerTask(
                title = title,
                time = "10:00 AM", // Placeholder time
                priority = priority,
                isCompleted = false
            )
        )
    }

    fun onTaskCompletedChange(task: PlannerTask, isCompleted: Boolean) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            tasks[index] = task.copy(isCompleted = isCompleted)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            AppHeader(title = "Planner", navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks for today!")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f) // Apply weight to fill available space
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onCompletedChange = { isCompleted ->
                                onTaskCompletedChange(task, isCompleted)
                            }
                        )
                    }
                }
            }
        } // This correctly closes the Column
    } // This correctly closes the Scaffold content

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, priority ->
                onAddTask(title, priority)
                showDialog = false
            }
        )
    }
} // <-- This is the ONLY brace that should close the PlannerScreen function.

// --- All helper composables are now top-level functions in the file ---

@Composable
fun TaskItem(task: PlannerTask, onCompletedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCompletedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = task.time,
                style = MaterialTheme.typography.bodySmall,
                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        PriorityIndicator(priority = task.priority)
    }
}

@Composable
fun PriorityIndicator(priority: TaskPriority) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(
                color = when (priority) {
                    TaskPriority.HIGH -> Color.Red
                    TaskPriority.MEDIUM -> Color(0xFFFFA500) // Orange
                    TaskPriority.LOW -> Color.Blue
                },
                shape = CircleShape
            )
    )
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, TaskPriority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text("Add New Task", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box {
                    OutlinedTextField(
                        value = priority.name,
                        onValueChange = {},
                        label = { Text("Priority") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                "Priority dropdown",
                                Modifier.clickable { expanded = true })
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // FIX: Use .entries which is the recommended replacement for .values()
                        TaskPriority.entries.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    priority = p
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onConfirm(title, priority) }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
