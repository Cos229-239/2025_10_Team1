package com.example.myapplication
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.WavingHand
import com.example.myapplication.ui.BreathingExerciseScreen

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.*
import com.example.myapplication.ui.theme.MyApplicationTheme
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
    object EditProfile : Screen("edit_profile")
    object Menu : Screen("menu")
    object Music : Screen("music")

    object StretchDetail : Screen("stretch_detail/{name}") {
        fun createRoute(name: String) = "stretch_detail/$name"
    }
}


data class ChartSegment(val reason: String, val value: Float, val color: Color)
data class Tip(val title: String, val url: String, val imageResId: Int? = null)

data class Stretch(
    val name: String,
    val description: String,
    val detailedDescription: String,
    @DrawableRes val imageRes: Int,
    val icon: ImageVector
)

val stretchDataMap = listOf(
    Stretch(
        name = "Neck Tilt",
        description = "Gently tilt your head from side to side.",
        detailedDescription = "Sit or stand tall. Gently tilt your head towards your right shoulder, holding for 15-20 seconds. Feel the stretch on the left side of your neck. Return to center and repeat on the other side.",
        imageRes = R.drawable.stretch_neck,
        icon = androidx.compose.material.icons.outlined.Person
    ),
    Stretch(
        name = "Shoulder Rolls",
        description = "Roll your shoulders backwards, then forwards.",
        detailedDescription = "Inhale and lift your shoulders up towards your ears. Exhale and roll them back and down. Repeat this motion 5 times, then reverse the direction and roll them forwards 5 times.",
        imageRes = R.drawable.stretch_shoulders,
        icon = androidx.compose.material.icons.outlined.AccessibilityNew
    ),
    Stretch(
        name = "Chest Opener",
        description = "Clasp hands behind you to open the chest.",
        detailedDescription = "Stand and clasp your hands behind your back, interlocking your fingers. Straighten your arms and gently lift your hands upwards. You should feel a stretch across your chest and the front of your shoulders. Hold for 20 seconds.",
        imageRes = R.drawable.stretch_chest,
        icon = androidx.compose.material.icons.outlined.SelfImprovement
    ),
    Stretch(
        name = "Wrist Stretch",
        description = "Gently pull your fingers back.",
        detailedDescription = "Extend your right arm in front of you with your palm facing up. With your left hand, gently bend your right fingers down towards the floor. Hold for 15 seconds. Switch hands and repeat.",
        imageRes = R.drawable.stretch_wrist,
        icon = androidx.compose.material.icons.outlined.WavingHand
    ),
    Stretch(
        name = "Spinal Twist",
        description = "While seated, gently twist your torso.",
        detailedDescription = "Sit sideways in a chair, facing right. Keep your feet flat on the floor. Inhale to lengthen your spine, then exhale and twist your torso to the right, using the chair back for leverage. Hold for 20 seconds, then switch sides.",
        imageRes = R.drawable.stretch_spine,
        icon = androidx.compose.material.icons.outlined.Chair
    )
).associateBy { it.name }

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

    // State for the selected music URI, hoisted to the highest level
    var selectedMusicUri by remember { mutableStateOf<Uri?>(null) }

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
            composable(Screen.EditProfile.route) { EditProfileScreen(navController = navController) }
            composable(Screen.Menu.route) { MenuScreen(navController = navController) }
            composable(Screen.Music.route) {
                MusicScreen(
                    navController = navController,
                    currentMusicUri = selectedMusicUri,
                    onMusicSelect = { selectedMusicUri = it }
                )
            }

            composable(
                route = Screen.StretchDetail.route,
                arguments = listOf(navArgument("name") { type = NavType.StringType })
            ) { backStackEntry ->
                val stretchName = backStackEntry.arguments?.getString("name")
                val stretch = stretchDataMap[stretchName]
                if (stretch != null) {
                    StretchDetailScreen(stretch = stretch, navController = navController)
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Stretch not found!")
                    }
                }
            }
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
                        .size(40.dp)
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

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24, Screen.Home.route),
        BottomNavItem("Breakroom", androidx.compose.material.icons.outlined.SelfImprovement, Screen.Breakroom.route),
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
