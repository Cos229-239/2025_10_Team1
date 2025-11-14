package com.example.myapplication.ui1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ChartSegment
import com.example.myapplication.PlannerTask
import com.example.myapplication.R
import com.example.myapplication.Tip


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
        Box(modifier = Modifier.size(20.dp).background(color, shape = CircleShape))
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
