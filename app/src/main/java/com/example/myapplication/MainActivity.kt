package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.*

// --- Data Classes ---
data class PlannerTask(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val time: String,
    val color: Color,
    var isCompleted: Boolean
)

data class BottomNavItem(val label: String, val iconResId: Int, val route: String)

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Log : Screen("log")
    object Planner : Screen("planner")
    object Insights : Screen("insights")
    object Account : Screen("account")
}

data class ChartSegment(val reason: String, val value: Float, val color: Color)
// UPDATED: Added a url property
data class Tip(val title: String, val url: String, val imageResId: Int? = null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    // State is "lifted" to here. This is the single source of truth for the tasks.
    val tasks = remember {
        mutableStateListOf(
            PlannerTask(title = "Meeting with Team", time = "10:00 AM", color = Color(0xFFB2EBF2), isCompleted = false),
            PlannerTask(title = "Lunch with John", time = "12:00 PM", color = Color(0xFFFFF9C4), isCompleted = false),
            PlannerTask(title = "Submit Project Report", time = "3:00 PM", color = Color(0xFFC8E6C9), isCompleted = false),
            PlannerTask(title = "Gym Session", time = "5:00 PM", color = Color(0xFFFFCCBC), isCompleted = true)
        )
    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Planner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Log.route) { LogScreen() }
            // Pass the state down to the PlannerScreen
            composable(Screen.Planner.route) {
                PlannerScreen(
                    tasks = tasks,
                    onTaskCompletedChange = { task, isCompleted ->
                        val index = tasks.indexOf(task)
                        if (index != -1) {
                            tasks[index] = tasks[index].copy(isCompleted = isCompleted)
                        }
                    },
                    onAddTask = { title ->
                        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                        tasks.add(PlannerTask(title = title, time = currentTime, color = Color(0xFFE1BEE7), isCompleted = false))
                    }
                )
            }
            // Pass the state down to the InsightsScreen
            composable(Screen.Insights.route) { InsightsScreen(tasks = tasks) }
            composable(Screen.Account.route) { AccountScreen() }
        }
    }
}

// --- INSIGHTS SCREEN ---
@Composable
fun InsightsScreen(tasks: List<PlannerTask>, modifier: Modifier = Modifier) {
    // Calculate stats based on the passed-in tasks
    val completedTasks = tasks.count { it.isCompleted }
    val totalTasks = tasks.size
    val completedHours = completedTasks * 2 // Fake hours for now

    // Static data for the donut chart
    val segments = listOf(
        ChartSegment("Reason", 0.55f, Color(0xFFA4EBF3)),
        ChartSegment("Reason", 0.25f, Color(0xFFFFF9C4)),
        ChartSegment("Reason", 0.20f, Color(0xFFF8BBD0))
    )

    // UPDATED: Actionable tips now include URLs
    val tips = listOf(
        Tip("Use the 2-Minute Rule", "https://jamesclear.com/how-to-stop-procrastinating", R.drawable.ic_tip_placeholder),
        Tip("Break Tasks Down", "https://www.beyondbooksmart.com/executive-functioning-strategies-blog/how-to-break-big-tasks-down-into-smaller-steps-to-avoid-overwhelm", R.drawable.ic_tip_placeholder),
        Tip("Reward Your Progress", "https://www.youtube.com/watch?v=eACvI6y2qso", R.drawable.ic_tip_placeholder),
        Tip("Minimize Distractions", "https://www.unr.edu/writing-speaking-center/student-resources/strategies-for-reducing-distractions-and-improving-focus", R.drawable.ic_tip_placeholder),
        Tip("Practice Self-Compassion", "https://www.youtube.com/watch?v=11U0h0DPu7Q", R.drawable.ic_tip_placeholder)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        InsightsHeader()
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "This week's Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(20.dp))

        InsightsChart(segments = segments)
        Spacer(modifier = Modifier.height(32.dp))

        // Use the dynamic data in the SummaryCard
        SummaryCard(completedHours = completedHours, completedTasks = totalTasks)
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Tips to keep you on track",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(16.dp))

        TipsSection(tips = tips)
        Spacer(modifier = Modifier.height(16.dp))
    }
}


// --- INSIGHTS SCREEN COMPOSABLES ---

@Composable
fun InsightsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Insights",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(120.dp) // Adjusted size
            )
        }
    }
}


@Composable
fun InsightsChart(segments: List<ChartSegment>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // We replace .size() with .aspectRatio(1f) to force a square shape
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            DonutChart(segments = segments)
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(modifier = Modifier.weight(1f)) {
            segments.forEach { segment ->
                LegendItem(color = segment.color, text = segment.reason)
            }
        }
    }
}


@Composable
fun DonutChart(segments: List<ChartSegment>, strokeWidth: Float = 40f) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f

    Canvas(modifier = Modifier.fillMaxSize()) {
        segments.forEach { segment ->
            val sweepAngle = (segment.value / totalValue) * 360f
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                size = Size(size.width, size.height)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SummaryCard(completedHours: Int, completedTasks: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF33691E))) {
                    append("Great Job!!")
                }
                append(" You completed ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$completedHours hours")
                }
                append(" of focus this week and completed ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$completedTasks tasks.")
                }
            },
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TipsSection(tips: List<Tip>) {
    val uriHandler = LocalUriHandler.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(tips.size) { index ->
            TipCard(
                tip = tips[index],
                onClick = { uriHandler.openUri(tips[index].url) }
            )
        }
    }
}

// UPDATED: TipCard now has an onClick parameter
@Composable
fun TipCard(tip: Tip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable(onClick = onClick), // Make the card clickable
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0).copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            if (tip.imageResId != null) {
                Icon(
                    painter = painterResource(id = tip.imageResId),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = tip.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

// --- PLANNER SCREEN ---
@Composable
fun PlannerScreen(
    tasks: List<PlannerTask>,
    onTaskCompletedChange: (PlannerTask, Boolean) -> Unit,
    onAddTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onTaskAdd = { taskTitle ->
                onAddTask(taskTitle)
                showAddTaskDialog = false
            }
        )
    }

    // The modifier order here is critical and has been corrected.
    Column(
        modifier = modifier
            .fillMaxSize()
            // 1. Apply system bar padding FIRST to the outer container.
            .padding(WindowInsets.systemBars.asPaddingValues())
            // 2. Then make the inner content scrollable.
            .verticalScroll(rememberScrollState())
            // 3. Finally, apply horizontal padding to the content itself.
            .padding(horizontal = 16.dp)
    ) {
        TopHeader()
        WelcomeMessage(name = "User")
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        tasks.forEach { task ->
            TaskItem(
                task = task,
                onTaskCompletedChange = { isCompleted ->
                    onTaskCompletedChange(task, isCompleted)
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // This padding just provides a bit of visual space from the bottom of the scroll area.
        AddTaskButton(
            modifier = Modifier.padding(bottom = 16.dp),
            onAddTask = { showAddTaskDialog = true }
        )
    }
}





// --- PLANNER SCREEN COMPOSABLES ---

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24, Screen.Home.route),
        BottomNavItem("Log", R.drawable.log_file_1, Screen.Log.route),
        BottomNavItem("Planner", R.drawable.calendar__1__1, Screen.Planner.route),
        BottomNavItem("Insights", R.drawable.chart_histogram__1__1, Screen.Insights.route),
        BottomNavItem("Account", R.drawable.user__1__1, Screen.Account.route)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = item.iconResId), contentDescription = item.label, modifier = Modifier.size(28.dp)) },
                label = { Text(text = item.label, fontSize = 12.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

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
            Button(onClick = { if (taskTitle.isNotBlank()) { onTaskAdd(taskTitle) } }, enabled = taskTitle.isNotBlank()) {
                Text("Add")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun TopHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Daily Planner", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Normal)
            Icon(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.padding(start = 20.dp).size(120.dp)
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
fun TaskItem(task: PlannerTask, onTaskCompletedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(task.color.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Switch(checked = task.isCompleted, onCheckedChange = onTaskCompletedChange)
        Column(horizontalAlignment = Alignment.End) {
            Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = task.time, color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}

@Composable
fun AddTaskButton(modifier: Modifier = Modifier, onAddTask: () -> Unit) {
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
        Text(text = "Add Task", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp))
    }
}

// --- Placeholder Screens ---
@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Home Screen") }
}

@Composable
fun LogScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Log Screen") }
}

@Composable
fun AccountScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Account Screen") }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun InsightsScreenPreview() {
    MyApplicationTheme {
        // Pass a sample list for the preview to work
        InsightsScreen(tasks = listOf(
            PlannerTask(
                title = "Preview Task 1",
                time = "10:00 AM",
                color = Color.Red,
                isCompleted = true
            ),
            PlannerTask(
                title = "Preview Task 2",
                time = "12:00 PM",
                color = Color.Blue,
                isCompleted = false
            )
        ))
    }
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        MainApp()
    }
}
