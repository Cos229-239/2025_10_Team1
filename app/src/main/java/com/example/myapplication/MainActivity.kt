package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

// --- Data Classes ---
data class Task(val title: String, val time: String, val color: Color, val isCompleted: Boolean)
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
    val tasks = listOf(
        Task("Finish Report", "9:00 AM", Color(0xFFB2EBF2), false),
        Task("Go to the Gym", "11:00 AM", Color(0xFFFFF9C4), false),
        Task("Read Book", "1:00 PM", Color(0xFFE1BEE7), false),
        Task("Project Meeting", "3:00 PM", Color(0xFFFFDAB9), false)
    )

    var selectedDay by remember { mutableStateOf("Tue") }
    var userName by remember { mutableStateOf("Name") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userName = getUserDisplayName(context)
    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar() },
        modifier = modifier
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopHeader()
                WelcomeMessage(name = userName)
                DaySelector(
                    selectedDay = selectedDay,
                    onDaySelected = { day -> selectedDay = day }
                )
                Spacer(modifier = Modifier.height(24.dp))
                tasks.forEach { task -> TaskItem(task = task) }
                Spacer(modifier = Modifier.height(80.dp))
            }


            AddTaskButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}




@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
        // horizontalArrangement is removed
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
        // IconButton for the menu is removed
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
fun DaySelector(selectedDay: String, onDaySelected: (String) -> Unit) {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            DayChip(
                day = day,
                isSelected = day == selectedDay,
                onDayClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
fun DayChip(day: String, isSelected: Boolean, onDayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onDayClick)
            .background(if (isSelected) Color.DarkGray else Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
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
fun AddTaskButton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE0F7E0), shape = RoundedCornerShape(16.dp))
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

private fun getUserDisplayName(context: Context): String {
    try {
        val cursor = context.contentResolver.query(
            ContactsContract.Profile.CONTENT_URI,
            arrayOf(ContactsContract.Profile.DISPLAY_NAME_PRIMARY),
            null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val name = cursor.getString(0)
            cursor.close()
            if (!name.isNullOrBlank()) {
                return name.split(" ")[0]
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "User"
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        PlannerScreen()
    }
}
