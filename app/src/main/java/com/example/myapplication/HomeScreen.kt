package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HeaderSection(userName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Home", style = MaterialTheme.typography.headlineLarge)
        Text("Today's Date", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Welcome back, $userName!",
            fontSize = 38.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp)
        )
    }
}

@Composable
fun InsightsChart() {
    Text(text = "Current Insights", style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp))
    Box(
        modifier = Modifier
            .padding(start = 26.dp, top = 20.dp)
            .size(147.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, shape = CircleShape)
        )
    }

    Box(
        modifier = Modifier
            .padding(start = 207.dp)
            .offset(y = (-127).dp)
            .size(20.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("")
    }

    Box(
        modifier = Modifier
            .padding(start = 207.dp, top = 18.dp)
            .offset(y = (-127).dp)
            .size(20.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("")
    }

    Box(
        modifier = Modifier
            .padding(start = 207.dp, top = 18.dp)
            .offset(y = (-127).dp)
            .size(20.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("")
    }
    Text(
        text = "Reason 1", style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, start = 190.dp)
            .offset(y = (-228).dp)
    )
    Text(
            text = "Reason 2", style = MaterialTheme.typography.bodyLarge,
    textAlign = TextAlign.Center,
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 15.dp, start = 190.dp)
        .offset(y = (-228).dp)
    )
    Text(
        text = "Reason 3", style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, start = 190.dp)
            .offset(y = (-228).dp)
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Column(modifier = Modifier.padding(20.dp)) {
        HeaderSection(userName = "RJ")
        Spacer(modifier = Modifier.height(24.dp))
        InsightsChart()
    }
}