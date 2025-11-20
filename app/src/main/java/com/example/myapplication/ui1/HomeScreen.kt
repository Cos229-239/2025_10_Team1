package com.example.myapplication.ui1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.ui1.theme.DonutChartPurple
import com.example.myapplication.ui1.theme.DonutChartTeal
import com.example.myapplication.ui1.theme.DonutChartYellow
import com.example.myapplication.ui1.theme.MyApplicationTheme
import com.example.myapplication.ui1.theme.PastelBlue
import com.example.myapplication.ui1.theme.PastelGreen
import com.example.myapplication.ui1.theme.PastelOrange
import com.example.myapplication.ui1.theme.PastelPurple
import com.example.myapplication.ui1.theme.PastelYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// --- Main Composable ---

@Composable
fun HomeScreen(navController: NavController) {
    // FIX 1: Define the standard purple-to-teal gradient.
    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(standardGradient) // Apply the standard gradient
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Spacer(modifier = Modifier.height(16.dp))
        HomeHeader(onMenuClick = { navController.navigate(Screen.Menu.route) })
        Spacer(modifier = Modifier.height(32.dp))

        // --- Welcome Message ---
        // FIX 2: Change text color to White for readability.
        Text(
            text = "Welcome back, Name!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // --- Current Insights Section ---
        Text(
            text = "Current Insights",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.8f), // Use semi-transparent white for subtitles
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        InsightsDonutChart()
        Spacer(modifier = Modifier.height(24.dp))

        // --- Motivational Message ---
        MotivationalMessage()
        Spacer(modifier = Modifier.height(32.dp))

        // --- Navigation Buttons Section ---
        Text(
            text = "Where would you like to go?",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.8f), // Use semi-transparent white for subtitles
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        NavigationButtons(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))
    }
}



// --- Sub-Components ---

@Composable
fun HomeHeader(onMenuClick: () -> Unit) {
    val currentDate = remember {
        SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // FIX 3: Update Header text colors.
                Text(text = "Home", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.tisense_icon_2),
                    contentDescription = "Logo",
                    modifier = Modifier.size(92.dp)
                )
            }
            Text(
                text = currentDate,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f) // Subdued white for the date
            )
        }
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(32.dp),
                tint = Color.White // Set icon tint to white
            )
        }
    }
}

@Composable
fun InsightsDonutChart() {
    val chartData = mapOf(
        DonutChartTeal to 0.4f,
        DonutChartYellow to 0.3f,
        DonutChartPurple to 0.3f
    )
    val reasons = listOf("Reason 1", "Reason 2", "Reason 3")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var startAngle = -90f
                chartData.forEach { (color, percentage) ->
                    val sweepAngle = 360 * percentage
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle - 4,
                        useCenter = false,
                        style = Stroke(width = 55f, cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngle
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            chartData.keys.zip(reasons).forEach { (color, reason) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // FIX 4: Update Insight text color.
                    Text(text = reason, fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}


@Composable
fun MotivationalMessage() {
    // This card's pastel color is fine and provides good contrast.
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PastelYellow),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Motivational Message :)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            color = Color.DarkGray // Ensure text inside card is readable
        )
    }
}

@Composable
fun NavigationButtons(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavButton("Planner", PastelGreen, Modifier.weight(1f)) { navController.navigate(Screen.Planner.route) }
            NavButton("Insights", PastelPurple, Modifier.weight(1f)) { navController.navigate(Screen.Insights.route) }
            NavButton("Breakroom", PastelOrange, Modifier.weight(1f)) { navController.navigate(Screen.Breakroom.route) }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavButton("My Log", PastelYellow, Modifier.weight(1f)) { navController.navigate(Screen.Log.route) }
            NavButton("Focus Session", PastelBlue, Modifier.weight(1f)) { navController.navigate(Screen.Focus.route) }
        }
    }
}

@Composable
fun NavButton(text: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    // The pastel colors and dark text on these buttons are fine.
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, textAlign = TextAlign.Center)
    }
}

// --- Preview ---

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(navController = rememberNavController())
    }
}
