package com.example.myapplication.ui1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerWheel(
    minutes: Int,
    seconds: Int,
    enabled: Boolean,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        NumberPicker(
            range = (0..59).toList(),
            value = minutes,
            onValueChange = onMinutesChanged,
            enabled = enabled
        )
        Text(
            text = ":",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.Black
        )
        NumberPicker(
            range = (0..59).toList(),
            value = seconds,
            onValueChange = onSecondsChanged,
            enabled = enabled
        )
    }
}

@Composable
fun NumberPicker(
    range: List<Int>,
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = range.indexOf(value).coerceAtLeast(0))

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && enabled) {
            val centerIndex = listState.firstVisibleItemIndex + 1
            if (centerIndex < range.size) {
                val newValue = range[centerIndex]
                onValueChange(newValue)
                listState.animateScrollToItem(centerIndex - 1)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.height(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = enabled
    ) {
        items(range.size + 2) { index ->
            val isPlaceholder = index == 0 || index == range.size + 1
            val itemValue = if (isPlaceholder) "" else range.getOrNull(index - 1)?.toString()?.padStart(2, '0') ?: ""
            Text(
                text = itemValue,
                fontSize = 24.sp,
                color = if (isPlaceholder) Color.Transparent else Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
