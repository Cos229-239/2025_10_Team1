package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

// A simple data class to hold task information
data class Task(
    val title: String,
    val time: String,
    val color: Color,
    val isCompleted: Boolean
)
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
    // Create a sample list of tasks for demonstration
    val tasks = listOf(
        Task("Finish Report", "9:00 AM", Color(0xFFB2EBF2), false),
        Task("Go to the Gym", "11:00 AM", Color(0xFFFFF9C4), false),
        Task("Read Book", "1:00 PM", Color(0xFFE1BEE7), false),
        Task("Project Meeting", "3:00 PM", Color(0xFFFFDAB9), false)
    )

    // Scaffold provides the basic material design layout structure
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar()
        },
        modifier = modifier
    ) { innerPadding ->
        // Main content goes here, using the padding from the Scaffold
        Column(
            modifier = Modifier
                .padding(innerPadding) // Important for content not to overlap with bars
                .padding(horizontal = 16.dp) // Add horizontal padding to the whole screen
        ) {
            // All the screen components will be called here
            TopHeader()
            WelcomeMessage(name = "Name")
            DaySelector()

            // Spacer to add some room
            Spacer(modifier = Modifier.height(16.dp))

            // Loop through the tasks and display each one
            tasks.forEach { task ->
                TaskItem(task = task)
            }

            AddTaskButton()
        }
    }
}


data class BottomNavItem(
    val label: String,
    val iconResId: Int
)

@Composable
fun AppBottomNavigationBar() {

    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24),
        BottomNavItem("Log", R.drawable.log_file_1),
        BottomNavItem("Planner", R.drawable.calendar__1__1),
        BottomNavItem("Insights", R.drawable.chart_histogram__1__1),
        // *** FIX 1: Corrected the closing parenthesis here ***
        BottomNavItem("Account", R.drawable.user__1__1)
    )
        var selectedItem by remember { mutableStateOf("Planner") }

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.iconResId), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == item.label,
                onClick = { selectedItem = item.label }
            )
        }
    }
}

@Composable
fun DaySelector() {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val selectedDay = "Tue"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            DayChip(day = day, isSelected = day == selectedDay)
        }
    }
}

@Composable
fun WelcomeMessage(name: String) {
    Text(
        text = "What are we doing today, $name?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu"
        )
    }
}

@Composable
fun AddTaskButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(Color(0xFFE0F7E0), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Task"
        )
        Text(
            text = "Add Task",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}



@Composable
fun TaskItem(task: Task) {
    var isChecked by remember { mutableStateOf(task.isCompleted) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                task.color.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
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
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}

@Composable
fun DayChip(day: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                color = if (isSelected) Color.DarkGray else Color.Transparent
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        PlannerScreen()
    }
}
