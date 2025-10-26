package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// --- Data Classes & Enums ---

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

data class PlannerTask(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val time: String,
    val priority: TaskPriority,
    var isCompleted: Boolean
)

data class BottomNavItem(val label: String, val icon: Any, val route: String)

data class MenuItem(val title: String, val color: Color, val route: String)

// UPDATED Screen Sealed Class
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Breakroom : Screen("breakroom")
    object Breathing : Screen("breathing")
    object Stretch : Screen("stretch")
    object Log : Screen("log")
    object Planner : Screen("planner")
    object Insights : Screen("insights")
    object Account : Screen("account")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile") // <-- ADDED
    object Menu : Screen("menu")
}


data class ChartSegment(val reason: String, val value: Float, val color: Color)
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
    val tasks = remember {
        mutableStateListOf(
            PlannerTask(title = "Submit Project Report", time = "3:00 PM", priority = TaskPriority.HIGH, isCompleted = false),
            PlannerTask(title = "Meeting with Team", time = "10:00 AM", priority = TaskPriority.MEDIUM, isCompleted = false),
            PlannerTask(title = "Lunch with John", time = "12:00 PM", priority = TaskPriority.LOW, isCompleted = false),
            PlannerTask(title = "Gym Session", time = "5:00 PM", priority = TaskPriority.LOW, isCompleted = true)
        )
    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        // UPDATED NavHost
        NavHost(
            navController = navController,
            startDestination = Screen.Planner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.Breakroom.route) { BreakroomScreen(navController = navController) }
            composable(Screen.Breathing.route) { BreathingExerciseScreen(navController = navController) }
            composable(Screen.Stretch.route) { StretchExerciseScreen(navController = navController) }
            composable(Screen.Log.route) { LogScreen(navController = navController) }
            composable(Screen.Planner.route) {
                PlannerScreen(
                    tasks = tasks,
                    onTaskCompletedChange = { task, isCompleted ->
                        val index = tasks.indexOf(task)
                        if (index != -1) {
                            tasks[index] = tasks[index].copy(isCompleted = isCompleted)
                        }
                    },
                    onAddTask = { title, priority ->
                        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                        tasks.add(PlannerTask(title = title, time = currentTime, priority = priority, isCompleted = false))
                    },
                    navController = navController
                )
            }
            composable(Screen.Insights.route) { InsightsScreen(tasks = tasks, navController = navController) }
            composable(Screen.Account.route) { AccountScreen(navController = navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController = navController) }
            composable(Screen.EditProfile.route) { EditProfileScreen(navController = navController) } // <-- ADDED
            composable(Screen.Menu.route) { MenuScreen(navController = navController) }
        }
    }
}

// --- SHARED COMPOSABLES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String, navController: NavController) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    painter = painterResource(id = R.drawable.tisense_icon_2),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(92.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Menu.route) }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}


// --- MENU SCREEN ---

@Composable
fun MenuScreen(navController: NavController) {
    val menuItems = listOf(
        MenuItem("Home", Color(0xFFADD8E6), Screen.Home.route),
        MenuItem("My Log", Color(0xFFF0E68C), Screen.Log.route),
        MenuItem("Insights", Color(0xFFD8BFD8), Screen.Insights.route),
        MenuItem("Breakroom", Color(0xFFFFDAB9), Screen.Breakroom.route),
        MenuItem("Planner", Color(0xFFC1E1C1), Screen.Planner.route),
        MenuItem("Focus Session", Color(0xFFADD8E6), Screen.Home.route),
        MenuItem("Account", Color(0xFFF0E68C), Screen.Account.route),
        MenuItem("Settings", Color(0xFFD8BFD8), Screen.Settings.route)
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = "Done",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            menuItems.forEach { item ->
                MenuButton(
                    item = item,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun MenuButton(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = item.color.copy(alpha = 0.8f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// --- SETTINGS SCREEN (FUNCTIONAL BUTTONS) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .verticalScroll(rememberScrollState())
    ) {
        // Custom Header for Settings
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )

        // Profile Header - Navigates to Edit Profile
        SettingsProfileHeader(
            name = "Kapil Mohan",
            email = "Edit personal details",
            onClick = { navController.navigate(Screen.EditProfile.route) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dark Mode Toggle
        SettingSwitchItem(
            title = "Dark Mode",
            icon = Icons.Outlined.NightsStay,
            iconBgColor = Color.DarkGray,
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it } // State is now updated
        )

        // Profile Section
        SettingsGroupHeader("Profile")
        SettingsItem(
            title = "Edit Profile",
            icon = Icons.Outlined.Person,
            iconBgColor = Color(0xFFFFA726), // Orange
            onClick = { navController.navigate(Screen.EditProfile.route) } // Navigates
        )
        SettingsItem(
            title = "Change Password",
            icon = Icons.Outlined.Lock,
            iconBgColor = Color(0xFF42A5F5), // Blue
            onClick = { navController.navigate(Screen.EditProfile.route) } // Navigates (placeholder)
        )

        // Notifications Section
        SettingsGroupHeader("Notifications")
        SettingSwitchItem(
            title = "Notifications",
            icon = Icons.Filled.Notifications,
            iconBgColor = Color(0xFF66BB6A), // Green
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it } // State is now updated
        )

        // Regional Section
        SettingsGroupHeader("Regional")
        SettingsItem(
            title = "Language",
            icon = Icons.Outlined.Translate,
            iconBgColor = Color(0xFF7E57C2), // Purple
            onClick = { navController.navigate(Screen.EditProfile.route) } // Navigates (placeholder)
        )

        // Logout
        SettingsItem(
            title = "Logout",
            icon = Icons.AutoMirrored.Filled.Logout,
            iconBgColor = Color(0xFFEF5350), // Red
            onClick = {
                // Simulate logout by returning to the main screen
                navController.navigate(Screen.Planner.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )

        // App Version
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "App ver 2.0.1",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SettingsProfileHeader(name: String, email: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile_placeholder),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(top = 16.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
    }
}


@Composable
fun SettingSwitchItem(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) } // Make the whole row clickable
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Edit Profile Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }
}


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
        Spacer(modifier = Modifier.height(48.dp))
        BreakTimer(
            remainingTimeSeconds = remainingTimeSeconds,
            isRunning = isRunning,
            onToggle = { isRunning = !isRunning }
        )
        Spacer(modifier = Modifier.height(48.dp))
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
        ActivityButton(
            text = "Play Music",
            icon = Icons.Outlined.MusicNote,
            color = Color(0xFFFFE0B2),
            modifier = Modifier.fillMaxWidth(0.8f)
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


// --- BREATHING EXERCISE SCREEN ---

@Composable
fun BreathingExerciseScreen(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing_transition")
    val size by infiniteTransition.animateFloat(
        initialValue = 150f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_size"
    )

    val instructionState by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 8000
                0f at 0
                1f at 4000
                1f at 8000
            },
            repeatMode = RepeatMode.Restart
        ), label = "breathing_text"
    )
    val instructionText = if (instructionState < 1f) "Breathe In..." else "Breathe Out..."

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF89CFF0), Color(0xFFB6D0E2))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(32.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = instructionText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(100.dp))
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .background(Color.White.copy(alpha = 0.3f), shape = CircleShape)
            )
        }
    }
}

// --- STRETCH EXERCISE SCREEN ---
@Composable
fun StretchExerciseScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE6E6FA), Color(0xFFD8BFD8))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(32.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AccessibilityNew,
                contentDescription = "Stretch Icon",
                modifier = Modifier.size(80.dp),
                tint = Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Stretch Exercises",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This screen will contain guided stretching exercises and poses. More content coming soon!",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}


// --- INSIGHTS SCREEN ---
@Composable
fun InsightsScreen(tasks: List<PlannerTask>, navController: NavController, modifier: Modifier = Modifier) {
    val completedTasks = tasks.count { it.isCompleted }
    val totalTasks = tasks.size
    val completedHours = completedTasks * 2 // Fake hours for now
    val segments = listOf(
        ChartSegment("Reason", 0.55f, Color(0xFFA4EBF3)),
        ChartSegment("Reason", 0.25f, Color(0xFFFFF9C4)),
        ChartSegment("Reason", 0.20f, Color(0xFFF8BBD0))
    )
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
        AppHeader(title = "Insights", navController = navController)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "This week's Insights", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(20.dp))
        InsightsChart(segments = segments)
        Spacer(modifier = Modifier.height(32.dp))
        SummaryCard(completedHours = completedHours, completedTasks = totalTasks)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Tips to keep you on track", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(16.dp))
        TipsSection(tips = tips)
        Spacer(modifier = Modifier.height(16.dp))
    }
}


// --- INSIGHTS SCREEN COMPOSABLES ---
@Composable
fun InsightsChart(segments: List<ChartSegment>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        Box(modifier = Modifier
            .size(20.dp)
            .background(color, shape = CircleShape))
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
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF33691E))) { append("Great Job!!") }
                append(" You completed ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$completedHours hours") }
                append(" of focus this week and completed ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$completedTasks tasks.") }
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

@Composable
fun TipCard(tip: Tip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
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
    onAddTask: (String, TaskPriority) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onTaskAdd = { taskTitle, priority ->
                onAddTask(taskTitle, priority)
                showAddTaskDialog = false
            }
        )
    }

    val sortedTasks = tasks.sortedBy { it.priority.ordinal }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        AppHeader(title = "Planner", navController = navController)
        WelcomeMessage(name = "User")
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        sortedTasks.forEach { task ->
            TaskItem(
                task = task,
                onTaskCompletedChange = { isCompleted ->
                    onTaskCompletedChange(task, isCompleted)
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
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
        BottomNavItem("Breakroom", Icons.Outlined.SelfImprovement, Screen.Breakroom.route),
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
                icon = {
                    when (val icon = item.icon) {
                        is Int -> Icon(painter = painterResource(id = icon), contentDescription = item.label, modifier = Modifier.size(28.dp))
                        is ImageVector -> Icon(imageVector = icon, contentDescription = item.label, modifier = Modifier.size(28.dp))
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        softWrap = false
                    )
                },
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
fun AddTaskDialog(onDismiss: () -> Unit, onTaskAdd: (String, TaskPriority) -> Unit) {
    var taskTitle by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Set Priority:", fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
        },
        dismissButton = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { if (taskTitle.isNotBlank()) onTaskAdd(taskTitle, TaskPriority.HIGH) },
                        enabled = taskTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                    ) { Text("High") }
                    Button(
                        onClick = { if (taskTitle.isNotBlank()) onTaskAdd(taskTitle, TaskPriority.MEDIUM) },
                        enabled = taskTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) { Text("Medium") }
                    Button(
                        onClick = { if (taskTitle.isNotBlank()) onTaskAdd(taskTitle, TaskPriority.LOW) },
                        enabled = taskTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) { Text("Low") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
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
    val priorityColor = when (task.priority) {
        TaskPriority.HIGH -> Color(0xFFF44336)
        TaskPriority.MEDIUM -> Color(0xFFFF9800)
        TaskPriority.LOW -> Color(0xFF4CAF50)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(priorityColor, CircleShape)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = task.time, color = Color.DarkGray, fontSize = 14.sp)
        }
        Switch(checked = task.isCompleted, onCheckedChange = onTaskCompletedChange)
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
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        AppHeader(title = "Home", navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Home Screen")
        }
    }
}

@Composable
fun LogScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        AppHeader(title = "Log", navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Log Screen")
        }
    }
}

@Composable
fun AccountScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        AppHeader(title = "Account", navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Account Screen")
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun BreakroomScreenPreview() {
    MyApplicationTheme {
        BreakroomScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun BreathingExerciseScreenPreview() {
    MyApplicationTheme {
        BreathingExerciseScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun StretchExerciseScreenPreview() {
    MyApplicationTheme {
        StretchExerciseScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MyApplicationTheme {
        MenuScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MyApplicationTheme {
        SettingsScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun InsightsScreenPreview() {
    MyApplicationTheme {
        InsightsScreen(tasks = listOf(
            PlannerTask(title = "Preview Task 1", time = "10:00 AM", priority = TaskPriority.HIGH, isCompleted = true),
            PlannerTask(title = "Preview Task 2", time = "12:00 PM", priority = TaskPriority.MEDIUM, isCompleted = false)
        ), navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenPreview() {
    MyApplicationTheme {
        val tasks = remember {
            mutableStateListOf(
                PlannerTask(title = "High Prio", time = "10:00 AM", priority = TaskPriority.HIGH, isCompleted = false),
                PlannerTask(title = "Low Prio", time = "11:00 AM", priority = TaskPriority.LOW, isCompleted = false),
                PlannerTask(title = "Medium Prio", time = "12:00 PM", priority = TaskPriority.MEDIUM, isCompleted = false)
            )
        }
        PlannerScreen(tasks = tasks, onTaskCompletedChange = {_,_ ->}, onAddTask = {_,_ ->}, navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MyApplicationTheme {
        EditProfileScreen(navController = rememberNavController())
    }
}
