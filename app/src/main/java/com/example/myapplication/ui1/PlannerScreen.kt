package com.example.myapplication.ui1

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapplication.PlannerTask
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.TaskPriority
import com.example.myapplication.ui.theme.LightBlue
import com.example.myapplication.ui.theme.LightGreen
import com.example.myapplication.ui.theme.LightOrange
import com.example.myapplication.ui.theme.LightPurple
import com.example.myapplication.ui.theme.LightYellow
import java.util.Calendar
import java.util.TimeZone

// --- Data class for calendar events ---
data class CalendarEvent(val title: String, val startTime: Long)

// --- PLANNER SCREEN ---
@Composable
fun PlannerScreen(
    navController: NavController,
    tasks: List<PlannerTask>,
    onAddTask: (String, TaskPriority) -> Unit,
    onTaskCompletedChange: (PlannerTask, Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("Tue") }
    var calendarEvents by remember { mutableStateOf<List<CalendarEvent>>(emptyList()) }
    val context = LocalContext.current

    // --- CALENDAR PERMISSION & DATA LOGIC ---
    val calendarPermissions = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            calendarEvents = getCalendarEvents(context)
        }
    }

    LaunchedEffect(Unit) {
        val allPermissionsGranted = calendarPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allPermissionsGranted) {
            calendarEvents = getCalendarEvents(context)
        } else {
            launcher.launch(calendarPermissions)
        }
    }

    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(standardGradient)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // FIX: Pass navigation action to the header
            PlannerHeader(onMenuClick = { navController.navigate(Screen.Menu.route) })
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "What are we doing today, Name?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            DaySelector(selectedDay = selectedDay, onDaySelected = { selectedDay = it })
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (tasks.isNotEmpty()) {
                    itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
                        TaskItem(
                            task = task,
                            onCompletedChange = { isCompleted ->
                                onTaskCompletedChange(task, isCompleted)
                            },
                            color = taskColors[index % taskColors.size]
                        )
                    }
                } else {
                    item {
                        Text("No tasks for today!", color = Color.White.copy(alpha = 0.8f))
                    }
                }

                if (calendarEvents.isNotEmpty()) {
                    item { 
                        Text(
                            "From Your Calendar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(calendarEvents) { event ->
                        CalendarEventItem(event)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            AddTaskButton(onClick = { showDialog = true })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, priority ->
                onAddTask(title, priority)
                addEventToCalendar(context, title) // Add event to calendar
                showDialog = false
            }
        )
    }
}

// --- CALENDAR INTEGRATION FUNCTIONS ---

private fun getCalendarEvents(context: android.content.Context): List<CalendarEvent> {
    val eventList = mutableListOf<CalendarEvent>()
    val projection = arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART)
    val calendar = Calendar.getInstance()
    val startTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val endTime = calendar.timeInMillis
    val selection = "(${CalendarContract.Events.DTSTART} >= ?) AND (${CalendarContract.Events.DTEND} <= ?)"
    val selectionArgs = arrayOf(startTime.toString(), endTime.toString())

    try {
        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, null
        )?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
            val startTimeColumn = cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColumn)
                val start = cursor.getLong(startTimeColumn)
                eventList.add(CalendarEvent(title, start))
            }
        }
    } catch (e: SecurityException) {
        // Handle the case where the user revokes permission
    }
    return eventList
}

private fun addEventToCalendar(context: android.content.Context, title: String) {
    val calID: Long = getPrimaryCalendarId(context) ?: return

    val startMillis: Long = Calendar.getInstance().run {
        timeInMillis
    }
    val endMillis: Long = Calendar.getInstance().run {
        add(Calendar.HOUR, 1)
        timeInMillis
    }

    val values = ContentValues().apply {
        put(CalendarContract.Events.DTSTART, startMillis)
        put(CalendarContract.Events.DTEND, endMillis)
        put(CalendarContract.Events.TITLE, title)
        put(CalendarContract.Events.CALENDAR_ID, calID)
        put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
    }

    try {
        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    } catch (e: SecurityException) {
        // Handle case where user revokes permission
    }
}

private fun getPrimaryCalendarId(context: android.content.Context): Long? {
    val projection = arrayOf(CalendarContract.Calendars._ID)
    val selection = "(${CalendarContract.Calendars.IS_PRIMARY} = ?)"
    val selectionArgs = arrayOf("1")
    try {
        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionArgs, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getLong(0)
            }
        }
    } catch (e: SecurityException) {
        // Handle permission issue
    }
    return null
}


@Composable
fun PlannerHeader(onMenuClick: () -> Unit) { // FIX: Accept an onClick lambda
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Daily Planner",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "Logo",
                modifier = Modifier.size(92.dp),
                tint = Color.Unspecified
            )
        }
        // FIX: Use the passed-in onClick action
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }
    }
}

@Composable
fun DaySelector(selectedDay: String, onDaySelected: (String) -> Unit) {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            val isSelected = day == selectedDay
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onDaySelected(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.7f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

val taskColors = listOf(LightBlue, LightYellow, LightPurple, LightOrange)

@Composable
fun TaskItem(task: PlannerTask, onCompletedChange: (Boolean) -> Unit, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = task.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = task.isCompleted,
                onCheckedChange = onCompletedChange
            )
        }
    }
}

@Composable
fun CalendarEventItem(event: CalendarEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun AddTaskButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Task", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, TaskPriority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }

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
                Text("Add New Task", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(title, priority) },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
