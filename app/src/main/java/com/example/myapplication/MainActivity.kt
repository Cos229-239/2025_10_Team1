package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Data Classes ---
data class PlannerTask(
    val id: Long = System.currentTimeMillis(), // Unique ID for each task
    val title: String,
    val time: String,
    val color: Color,
    var isCompleted: Boolean // Make isCompleted a var
)

data class BottomNavItem(val label: String, val iconResId: Int)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PlannerScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun PlannerScreen(modifier: Modifier = Modifier) {
    val tasks = remember {
        mutableStateListOf(
            PlannerTask(title = "Meeting with Team", time = "10:00 AM", color = Color(0xFFB2EBF2), isCompleted = false),
            PlannerTask(title = "Lunch with John", time = "12:00 PM", color = Color(0xFFFFF9C4), isCompleted = false),
            PlannerTask(title = "Submit Project Report", time = "3:00 PM", color = Color(0xFFC8E6C9), isCompleted = false),
            PlannerTask(title = "Gym Session", time = "5:00 PM", color = Color(0xFFFFCCBC), isCompleted = true)
        )
    }

    // --- 1. State to control the dialog's visibility ---
    var showAddTaskDialog by remember { mutableStateOf(false) }

    // --- 2. Show the dialog when the state is true ---
    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onTaskAdd = { taskTitle ->
                val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                tasks.add(
                    PlannerTask(
                        title = taskTitle,
                        time = currentTime,
                        color = Color(0xFFE1BEE7), // You can randomize this color later
                        isCompleted = false
                    )
                )
                showAddTaskDialog = false // Close the dialog after adding
            }
        )
    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar() },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            TopHeader()
            WelcomeMessage(name = "User")


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Today's Tasks",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            tasks.forEachIndexed { index, task ->
                TaskItem(
                    task = task,
                    onTaskCompletedChange = { newCompletionState ->
                        tasks[index] = task.copy(isCompleted = newCompletionState)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. Update the button's action to show the dialog ---
            AddTaskButton(
                modifier = Modifier.padding(bottom = 16.dp),
                onAddTask = { showAddTaskDialog = true }
            )
        }
    }
}

// --- 4. NEW: Composable for the Add Task Dialog ---
@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onTaskAdd: (String) -> Unit) {
    var taskTitle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a New Task") },
        text = {
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onTaskAdd(taskTitle)
                    }
                },
                // The button is only enabled if the user has typed something
                enabled = taskTitle.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TopHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Daily Planner",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Normal
            )
            Icon(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}



@Composable
fun WelcomeMessage(name: String) {
    Text(
        text = "What are we doing today, $name?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(bottom = 36.dp)
    )
}

@Composable
fun TaskItem(
    task: PlannerTask,
    onTaskCompletedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                task.color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Switch(
            checked = task.isCompleted,
            onCheckedChange = onTaskCompletedChange
        )
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = task.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = task.time,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE0F7E0), shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onAddTask)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
        Text(
            text = "Add Task",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun AppBottomNavigationBar() {
    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24),
        BottomNavItem("Log", R.drawable.log_file_1),
        BottomNavItem("Planner", R.drawable.calendar__1__1),
        BottomNavItem("Insights", R.drawable.chart_histogram__1__1),
        BottomNavItem("Account", R.drawable.user__1__1)
    )
    var selectedItem by remember { mutableStateOf("Planner") }

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.label,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp
                    )
                },
                selected = selectedItem == item.label,
                onClick = { selectedItem = item.label }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        PlannerScreen()
    }
}
