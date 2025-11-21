package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui1.AccountScreen
import com.example.myapplication.ui1.BreakroomScreen
import com.example.myapplication.ui1.BreathingExerciseScreen
import com.example.myapplication.ui1.EditProfileScreen
import com.example.myapplication.ui1.FocusScreen
import com.example.myapplication.ui1.HomeScreen
import com.example.myapplication.ui1.InsightsScreen
import com.example.myapplication.ui1.LogScreen
import com.example.myapplication.ui1.MenuScreen
import com.example.myapplication.ui1.MusicScreen
import com.example.myapplication.ui1.PlannerScreen
import com.example.myapplication.ui1.SettingsScreen
import com.example.myapplication.ui1.StretchDetailScreen
import com.example.myapplication.ui1.StretchExerciseScreen
import com.example.myapplication.ui1.theme.MyApplicationTheme

// --- Data Classes & Enums ---

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

data class PlannerTask(
    val id: Long,
    val title: String,
    val time: String,
    val priority: TaskPriority,
    val isCompleted: Boolean
)

data class BottomNavItem(val label: String, val icon: Any, val route: String)

data class MenuItem(val title: String, val color: Color, val route: String)

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Breakroom : Screen("breakroom")
    object BreathingExercise : Screen("breathing_exercise")
    object Stretch : Screen("stretch")
    object Log : Screen("log")
    object Planner : Screen("planner")
    object Insights : Screen("insights")
    object Account : Screen("account")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
    object Menu : Screen("menu")
    object Music : Screen("music")
    object Focus : Screen("focus") // <- Added Focus Screen

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
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    tasks: List<PlannerTask>,
    onAddTask: (String, TaskPriority) -> Unit,
    onTaskCompletedChange: (PlannerTask, Boolean) -> Unit,
    stretchDataMap: Map<String, Stretch>,
    selectedMusicUri: Uri?,
    onMusicSelect: (Uri) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.Breakroom.route) { BreakroomScreen(navController = navController) }
        composable(Screen.BreathingExercise.route) { BreathingExerciseScreen(navController = navController) }
        composable(Screen.Stretch.route) { StretchExerciseScreen(navController = navController, stretches = stretchDataMap.values.toList()) }
        composable(Screen.Log.route) { LogScreen(userName = "Jonathan") }
        composable(Screen.Planner.route) {
            PlannerScreen(
                navController = navController,
                tasks = tasks,
                onAddTask = onAddTask,
                onTaskCompletedChange = onTaskCompletedChange
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
                onMusicSelect = onMusicSelect
            )
        }
        composable(Screen.Focus.route) { FocusScreen(navController = navController) } // <- Added Focus Screen route
        composable(Screen.StretchDetail.route) { backStackEntry ->
            val stretchName = backStackEntry.arguments?.getString("name")
            val stretch = stretchDataMap[stretchName]
            if (stretch != null) {
                StretchDetailScreen(stretch = stretch, navController = navController)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Stretch not found!") }
            }
        }
    }
}


@Composable
fun MainApp() {
    val navController = rememberNavController()
    var selectedMusicUri by remember { mutableStateOf<Uri?>(null) }
    var uniqueIdCounter by remember { mutableStateOf(4L) }





val stretchDataMap = remember {
        listOf(
            Stretch("Neck Tilt", "Gently tilt your head from side to side.", "Sit or stand tall. Gently tilt your head towards your right shoulder, holding for 15-20 seconds. Feel the stretch on the left side of your neck. Return to center and repeat on the other side.", R.drawable.stretch_neck, Icons.Outlined.Person),
            Stretch("Shoulder Rolls", "Roll your shoulders backwards, then forwards.", "Inhale and lift your shoulders up towards your ears. Exhale and roll them back and down. Repeat this motion 5 times, then reverse the direction and roll them forwards 5 times.", R.drawable.stretch_shoulders, Icons.Outlined.AccessibilityNew),
            Stretch("Chest Opener", "Clasp hands behind you to open the chest.", "Stand and clasp your hands behind your back, interlocking your fingers. Straighten your arms and gently lift your hands upwards. You should feel a stretch across your chest and the front of your shoulders. Hold for 20 seconds.", R.drawable.stretch_chest, Icons.Outlined.SelfImprovement),
            Stretch("Wrist Stretch", "Gently pull your fingers back.", "Extend your right arm in front of you with your palm facing up. With your left hand, gently bend your right fingers down towards the floor. Hold for 15 seconds. Switch hands and repeat.", R.drawable.stretch_wrist, Icons.Outlined.WavingHand),
            Stretch("Spinal Twist", "While seated, gently twist your torso.", "Sit sideways in a chair, facing right. Keep your feet flat on the floor. Inhale to lengthen your spine, then exhale and twist your torso to the right, using the chair back for leverage. Hold for 20 seconds, then switch sides.", R.drawable.stretch_spine, Icons.Outlined.Chair)
        ).associateBy { it.name }
    }

    val tasks = remember {
        mutableStateListOf(
            PlannerTask(id = 0L, title = "Finish Report", time = "9:00 AM", priority = TaskPriority.HIGH, isCompleted = false),
            PlannerTask(id = 1L, title = "Go to the Gym", time = "11:00 AM", priority = TaskPriority.MEDIUM, isCompleted = false),
            PlannerTask(id = 2L, title = "Read Book", time = "1:00 PM", priority = TaskPriority.LOW, isCompleted = false),
            PlannerTask(id = 3L, title = "Project Meeting", time = "3:00 PM", priority = TaskPriority.HIGH, isCompleted = true)
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController, currentRoute = currentRoute) }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            tasks = tasks,
            onAddTask = { title, priority ->
                tasks.add(
                    PlannerTask(
                        id = uniqueIdCounter++,
                        title = title,
                        time = "10:00 AM",
                        priority = priority,
                        isCompleted = false
                    )
                )
            },

            onTaskCompletedChange = { task, isCompleted ->
                val index = tasks.indexOf(task)
                if (index != -1) {
                    tasks[index] = task.copy(isCompleted = isCompleted)
                }
            },
            stretchDataMap = stretchDataMap,
            selectedMusicUri = selectedMusicUri,
            onMusicSelect = { uri -> selectedMusicUri = uri }
        )
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24, Screen.Home.route),
        BottomNavItem("Log", R.drawable.log_file_1, Screen.Log.route),
        BottomNavItem("Planner", R.drawable.calendar__1__1, Screen.Planner.route),
        BottomNavItem("Insights", R.drawable.chart_histogram__1__1, Screen.Insights.route),
        BottomNavItem("Account", R.drawable.user__1__1, Screen.Account.route)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    val iconModifier = Modifier.size(28.dp)
                    when (val icon = item.icon) {
                        is Int -> Icon(painter = painterResource(id = icon), contentDescription = item.label, modifier = iconModifier)
                        is ImageVector -> Icon(imageVector = icon, contentDescription = item.label, modifier = iconModifier)
                    }
                },
                label = { Text(text = item.label, fontSize = 12.sp, softWrap = false) },
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
